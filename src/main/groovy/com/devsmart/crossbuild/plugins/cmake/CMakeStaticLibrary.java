package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;

import javax.inject.Inject;

public class CMakeStaticLibrary implements ComponentWithCMakeArgs {

    private final String name;
    private final ListProperty<String> cmakeArgs;


    @Inject
    public CMakeStaticLibrary(String variantName, ObjectFactory objectFactory) {
        this.name = variantName;
        cmakeArgs = objectFactory.listProperty(String.class);

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ListProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

}
