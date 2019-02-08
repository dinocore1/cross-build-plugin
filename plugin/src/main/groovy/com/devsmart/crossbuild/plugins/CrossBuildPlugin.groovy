package com.devsmart.crossbuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class CrossBuildPlugin implements Plugin<Project> {

    CrossBuildExtention config

    @Override
    void apply(Project project) {
        config = project.extensions.create("crossbuild", CrossBuildExtention, project)
        project.afterEvaluate{
            //createFlintTasks()
        }

    }
}
