package com.devsmart.crossbuild.plugins.cmake

import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.plugins.TargetConfig
import com.devsmart.crossbuild.tasks.BuildCMakeTask
import com.devsmart.crossbuild.tasks.ConfigCMakeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Usage
import org.gradle.api.component.ComponentWithVariants
import org.gradle.api.provider.Provider
import org.gradle.language.cpp.internal.MainLibraryVariant
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent

class CrossBuildCMakeLibPlugin implements Plugin<Project> {

    CMakeLibExtention librarySpec

    @Override
    void apply(Project project) {
        project.pluginManager.apply('crossbuild')

        def cppApiUsage = project.objects.named(Usage.class, Usage.C_PLUS_PLUS_API)

        CMakeLibComponent mainComponent = new CMakeLibComponent(project.providers.provider({project.name}), cppApiUsage, project.configurations.headers)
        project.components.add(mainComponent)

        librarySpec = project.extensions.create("cmakelib", CMakeLibExtention, project, mainComponent)

        project.afterEvaluate {

            List<TargetConfig> targets = project.plugins.findPlugin('crossbuild').config.targets
            for(TargetConfig target : targets) {

                BuildTarget buildTarget = target.buildTarget
                String comboName = cMakeLibComponent.baseName.get().capitalize() + buildTarget.os.name.capitalize() + buildTarget.arch.name.capitalize()

                librarySpec.installDir = project.file("build/${comboName}/install")

                ConfigCMakeTask configTask = project.tasks.create("config${comboName}", ConfigCMakeTask)
                configTask.srcDir = librarySpec.srcDir
                configTask.buildDir = project.file("build/cmake/${comboName}")
                configTask.generator = 'Ninja'
                configTask.cmakeArgs.addAll(target.cmakeArgs)
                configTask.cmakeArgs.addAll(librarySpec.cmakeArgs)
                configTask.cmakeArgs.add("CMAKE_INSTALL_PREFIX=${librarySpec.installDir.get().asFile.absolutePath}")
                configTask.dependsOn(librarySpec.srcDir)

                BuildCMakeTask buildTask = project.tasks.create("assemble${comboName}", BuildCMakeTask)
                buildTask.group = 'Build'
                buildTask.generatedBy(configTask)
                buildTask.buildOutputs = project.fileTree(dir: buildTask.buildDir, include: '**/*')

                BuildCMakeTask installTask = project.tasks.create("install${comboName}", BuildCMakeTask)
                installTask.group = 'Install'
                installTask.target = 'install'
                installTask.generatedBy(configTask)
                installTask.buildOutputs = project.fileTree(dir: librarySpec.installDir, include: '**/*')
                installTask.dependsOn(buildTask)



            }

        }

    }

    private static class CMakeLibComponent implements PublicationAwareComponent {

        private final Provider<String> baseName
        private final MainLibraryVariant mainVariant

        CMakeLibComponent(Provider<String> baseName, Usage apiUsage, Configuration api) {
            this.baseName = baseName
            this.mainVariant = new MainLibraryVariant("api", apiUsage, api, null)
        }

        @Override
        Provider<String> getBaseName() {
            return baseName
        }

        @Override
        ComponentWithVariants getMainPublication() {
            return mainVariant
        }

        @Override
        String getName() {
            return "main"
        }
    }
}
