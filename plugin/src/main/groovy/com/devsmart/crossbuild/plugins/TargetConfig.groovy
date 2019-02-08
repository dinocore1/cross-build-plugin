package com.devsmart.crossbuild.plugins

import com.devsmart.crossbuild.BuildTarget
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

import javax.inject.Inject

class TargetConfig {

    final ListProperty<String> cmakeArgs
    final Property<BuildTarget> buildTarget

    @Inject
    TargetConfig(Project project) {
        cmakeArgs = project.objects.listProperty(String.class)
        buildTarget = project.objects.property(BuildTarget.class)
    }

    void os(String osStr) {

    }

    void arch(String arch) {

    }
}
