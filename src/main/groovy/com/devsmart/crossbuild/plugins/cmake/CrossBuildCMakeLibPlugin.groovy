package com.devsmart.crossbuild.plugins.cmake

import com.devsmart.crossbuild.Arch
import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.DefaultPublishableComponent
import com.devsmart.crossbuild.plugins.CrossBuildExtention
import com.devsmart.crossbuild.plugins.CrossBuildPlugin
import com.devsmart.crossbuild.plugins.TargetConfig
import com.devsmart.crossbuild.tasks.BuildCMakeTask
import com.devsmart.crossbuild.tasks.ConfigCMakeTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrossBuildCMakeLibPlugin implements Plugin<Project> {

    public static final EXTENTION = 'cmakelib'



    @Override
    void apply(Project project) {
        project.pluginManager.apply('crossbuild')
        CrossBuildExtention crossBuildExt = project.extensions.getByName(CrossBuildPlugin.EXTENSION_NAME)

        CMakeLibrarySpec mainLib = project.extensions.create(EXTENTION, CMakeLibrarySpec.class)
        mainLib.whenConfigured {

            mainLib.component = new MainCMakeLibraryComponent()
            mainLib.component.baseName = project.providers.provider({project.name})
            project.components.add(mainLib.component)

            crossBuildExt.targets.all {
                addVariants(project, mainLib, it)
            }
        }

    }

    private void addVariants(Project project, CMakeLibrarySpec mainLib, TargetConfig target) {
        addNamedVariant(project, mainLib, target, 'debug')
        addNamedVariant(project, mainLib, target, 'release')
    }

    private void addNamedVariant(Project project, CMakeLibrarySpec mainLib, TargetConfig targetCfg, String variantName) {

        mainLib.component.addVariant(new DefaultPublishableComponent.Builder(project)
                .withGroup(project.group)
                .withProjectName(project.name)
                .withVersion(project.version)
                .withBuildTarget(targetCfg.buildTarget(project.objects))
                .withVariantName(variantName)
                .build())

        String comboName = String.format('%s%s%s%s',
                project.name.capitalize(), variantName.capitalize(), targetCfg.os.capitalize(), targetCfg.arch.capitalize())

        File installDir = project.file("build/${comboName}/install")

        ConfigCMakeTask configTask = project.tasks.create("config${comboName}", ConfigCMakeTask) {
            srcDir = mainLib.srcDir
            buildDir = project.file("build/cmake/${comboName}")
            generator = 'Ninja'
            cmakeArgs = ["CMAKE_BUILD_TYPE=${variantName}", targetCfg.cmakeArgs, mainLib.cmakeArgs, "CMAKE_INSTALL_PREFIX=${installDir.absolutePath}"].flatten()

        }

        BuildCMakeTask buildTask = project.tasks.create("assemble${comboName}", BuildCMakeTask) {
            group = 'build'
            generatedBy(configTask)
            buildOutputs = project.fileTree(dir: configTask.buildDir, include: '**/*')
        }

        BuildCMakeTask installTask = project.tasks.create("install${comboName}", BuildCMakeTask) {
            group = 'Install'
            target = 'install'
            generatedBy(configTask)
            buildOutputs = project.fileTree(dir: installDir, include: '**/*')
            dependsOn(buildTask)
        }
    }

}
