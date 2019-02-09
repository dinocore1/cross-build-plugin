package com.devsmart.crossbuild;

import org.gradle.api.model.ObjectFactory;

public class BuildTargetFactory {

    private final ObjectFactory objectFactory;

    public BuildTargetFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public BuildTarget host() {
        //TODO: implement
        //stem.getProperty("os.name")
        //System.getProperty("os.arch")
        return null;
    }

    public OS os(String osName) {
        return objectFactory.named(OS.class, osName);
    }

    public Arch arch(String archName) {
        return objectFactory.named(Arch.class, archName);
    }


}
