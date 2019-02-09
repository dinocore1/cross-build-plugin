package com.devsmart.crossbuild.plugins.cmake


import org.gradle.api.Plugin
import org.gradle.api.Project

class CrossBuildCMakeLibPlugin implements Plugin<Project> {

    private CMakeExtention config

    @Override
    void apply(Project project) {
        project.pluginManager.apply('crossbuild')

        config = project.extensions.create("cmake", CMakeExtention, project)


    }
}
