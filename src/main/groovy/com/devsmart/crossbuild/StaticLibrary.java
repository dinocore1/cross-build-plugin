package com.devsmart.crossbuild;

import com.devsmart.crossbuild.plugins.TargetConfig;
import com.devsmart.crossbuild.plugins.cmake.ComponentWithCMakeArgs;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.language.cpp.internal.NativeVariantIdentity;
import org.gradle.nativeplatform.TargetMachine;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class StaticLibrary implements ComponentWithVariants, ComponentWithCMakeArgs {


    private final String name;
    private final List<TargetConfig> targets;
    private final ListProperty<String> cmakeArgs;
    private final NamedDomainObjectContainer<SoftwareComponent> variants;

    @Inject
    public StaticLibrary(String name, NamedDomainObjectContainer<SoftwareComponent> variants, ObjectFactory objectFactory) {
        this.name = name;
        this.targets = new ArrayList<>();
        this.cmakeArgs = objectFactory.listProperty(String.class);
        this.variants = variants;
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
