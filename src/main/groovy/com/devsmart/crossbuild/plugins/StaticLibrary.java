package com.devsmart.crossbuild.plugins;

import org.gradle.api.model.ObjectFactory;
import org.gradle.language.cpp.internal.NativeVariantIdentity;

import javax.inject.Inject;

public class StaticLibrary {

    private final String name;
    private final NativeVariantIdentity id;

    @Inject
    public StaticLibrary(String variantName, ObjectFactory objectFactory, NativeVariantIdentity id) {
        this.name = variantName;
        this.id = id;
    }


}
