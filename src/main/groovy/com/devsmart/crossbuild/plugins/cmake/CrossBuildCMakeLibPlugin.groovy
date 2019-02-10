package com.devsmart.crossbuild.plugins.cmake

import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.DefaultPublishableComponent
import com.devsmart.crossbuild.plugins.TargetConfig
import com.devsmart.crossbuild.tasks.BuildCMakeTask
import com.devsmart.crossbuild.tasks.ConfigCMakeTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrossBuildCMakeLibPlugin implements Plugin<Project> {

    CMakeLibExtention librarySpec

    @Override
    void apply(Project project) {
        project.pluginManager.apply('crossbuild')



        librarySpec = project.extensions.create("cmakelib", CMakeLibExtention, project)
        project.components.add(librarySpec)

        project.afterEvaluate {

            List<TargetConfig> targets = project.plugins.findPlugin('crossbuild').config.targets
            for(TargetConfig target : targets) {

                BuildTarget buildTarget = target.buildTarget

                librarySpec.addVariant(new DefaultPublishableComponent.Builder(project)
                        .withGroup(project.group)
                        .withProjectName(project.name)
                        .withVersion(project.version)
                        .withBuildTarget(buildTarget)
                        .withVariantName("release")
                        .build())

                librarySpec.addVariant(new DefaultPublishableComponent.Builder(project)
                        .withGroup(project.group)
                        .withProjectName(project.name)
                        .withVersion(project.version)
                        .withBuildTarget(buildTarget)
                        .withVariantName("debug")
                        .build())

                String comboName = librarySpec.baseName.get().capitalize() + buildTarget.os.name.capitalize() + buildTarget.arch.name.capitalize()

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

}
