package com.devsmart.crossbuild.plugins;

import org.gradle.api.model.ObjectFactory;
import org.gradle.language.cpp.internal.NativeVariantIdentity;
import org.gradle.language.nativeplatform.internal.ComponentWithNames;
import org.gradle.language.nativeplatform.internal.Names;

import javax.inject.Inject;

public class StaticLibrary implements ComponentWithNames {

    private final Names names;
    private final NativeVariantIdentity id;

    @Inject
    public StaticLibrary(Names names, ObjectFactory objectFactory, NativeVariantIdentity id) {
        this.names = names;
        this.id = id;
    }


    @Override
    public Names getNames() {
        return names;
    }

    @Override
    public String getName() {
        return names.getName();
    }
}
