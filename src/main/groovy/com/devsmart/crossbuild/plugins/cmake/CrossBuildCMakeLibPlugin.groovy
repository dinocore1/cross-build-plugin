package com.devsmart.crossbuild.plugins.cmake

import com.devsmart.crossbuild.Arch
import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.DefaultPublishableComponent
import com.devsmart.crossbuild.plugins.CrossBuildExtention
import com.devsmart.crossbuild.plugins.CrossBuildPlugin
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

                def targetInfo = delegate

                mainLib.component.addVariant(new DefaultPublishableComponent.Builder(project)
                        .withGroup(project.group)
                        .withProjectName(project.name)
                        .withVersion(project.version)
                        .withBuildTarget(targetInfo.buildTarget(project.objects))
                        .withVariantName('debug')
                        .build())

                String comboName = String.format('%s%s%s',
                        project.name.capitalize(), targetInfo.os.capitalize(), targetInfo.arch.capitalize())

                File installDir = project.file("build/${comboName}/install")

                def configTask = project.tasks.register("config${comboName}", ConfigCMakeTask) {
                    srcDir = mainLib.srcDir
                    buildDir = project.file("build/cmake/${comboName}")
                    generator = 'Ninja'
                    cmakeArgs = [targetInfo.cmakeArgs, mainLib.cmakeArgs, "CMAKE_INSTALL_PREFIX=${installDir.absolutePath}"].flatten()

                }

                def buildTask = project.tasks.register("assemble${comboName}", BuildCMakeTask) {
                    group = 'build'
                    generatedBy(configTask.get())
                    buildOutputs = project.fileTree(dir: buildDir, include: '**/*')
                }

                def installTask = project.tasks.register("install${comboName}", BuildCMakeTask) {
                    group = 'Install'
                    target = 'install'
                    generatedBy(configTask.get())
                    buildOutputs = project.fileTree(dir: installDir, include: '**/*')
                    dependsOn(buildTask.get())
                }

            }
        }






    }

}
