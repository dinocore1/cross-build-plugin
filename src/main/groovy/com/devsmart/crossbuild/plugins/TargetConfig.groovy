package com.devsmart.crossbuild.plugins

import com.devsmart.crossbuild.Arch
import com.devsmart.crossbuild.BuildTarget
import com.devsmart.crossbuild.OS
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.nativeplatform.MachineArchitecture
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.TargetMachine
import org.gradle.nativeplatform.TargetMachineBuilder
import org.gradle.nativeplatform.internal.DefaultTargetMachineFactory

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

    TargetMachine buildTargetMachine(ObjectFactory objectFactory) {
        return new DefaultTargetMachineFactory(objectFactory).os(os).architecture(arch)
    }
}
