package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class CMakeStaticLibrary implements ComponentWithVariants, ComponentWithCMakeArgs {


    private final String name;
    private final List<CMakeTargetBuildVariant> targets;
    private final ListProperty<String> cmakeArgs;
    private final NamedDomainObjectContainer<SoftwareComponent> variants;

    @Inject
    public CMakeStaticLibrary(String name, NamedDomainObjectContainer<SoftwareComponent> variants, ObjectFactory objectFactory) {
        this.name = name;
        this.variants = variants;
        this.targets = new ArrayList<>();
        this.cmakeArgs = objectFactory.listProperty(String.class);

    }

    public String getName() {
        return name;
    }

    @Override
    public ListProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

    @Override
    public Set<SoftwareComponent> getVariants() {
        return variants;
    }



}
