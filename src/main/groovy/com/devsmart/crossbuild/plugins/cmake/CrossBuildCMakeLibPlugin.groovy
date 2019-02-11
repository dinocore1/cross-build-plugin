package com.devsmart.crossbuild.plugins.cmake

import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.DefaultPublishableComponent
import com.devsmart.crossbuild.plugins.CrossBuildExtention
import com.devsmart.crossbuild.plugins.CrossBuildPlugin
import com.devsmart.crossbuild.plugins.TargetConfig
import com.devsmart.crossbuild.tasks.BuildCMakeTask
import com.devsmart.crossbuild.tasks.ConfigCMakeTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrossBuildCMakeLibPlugin implements Plugin<Project> {

    public static final EXTENTION = 'cmakelibs'



    @Override
    void apply(Project project) {
        project.pluginManager.apply('crossbuild')

        NamedDomainObjectContainer<CMakeLibrarySpec> libs = project.container(CMakeLibrarySpec)
        project.extensions.add(EXTENTION, libs)

        CrossBuildExtention crossBuildExt = project.extensions.getByName(CrossBuildPlugin.EXTENSION_NAME)

        libs.all {

            def libraryInfo = delegate

            crossBuildExt.targets.all {

                def targetInfo = delegate

                String comboName = String.format('%s%s%s',
                        libraryInfo.name.capitalize(), targetInfo.os.capitalize(), targetInfo.arch.capitalize())

                File installDir = project.file("build/${comboName}/install")

                def configTask = project.tasks.register("config${comboName}", ConfigCMakeTask) {
                    srcDir = libraryInfo.srcDir
                    buildDir = project.file("build/cmake/${comboName}")
                    generator = 'Ninja'
                    cmakeArgs = [targetInfo.cmakeArgs, libraryInfo.cmakeArgs, "CMAKE_INSTALL_PREFIX=${installDir.absolutePath}"].flatten()

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
