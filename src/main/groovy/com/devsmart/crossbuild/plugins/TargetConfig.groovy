package com.devsmart.crossbuild.plugins

import com.devsmart.crossbuild.Arch
import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.OS
import org.gradle.api.model.ObjectFactory

class TargetConfig {

    String name
    List<String> cmakeArgs = []
    String os
    String arch

    TargetConfig(String name) {
        this.name = name
    }

    BuildTarget buildTarget(ObjectFactory objectFactory) {
        return new BuildTarget(objectFactory.named(OS.class, os), objectFactory.named(Arch.class, arch))
    }

}
