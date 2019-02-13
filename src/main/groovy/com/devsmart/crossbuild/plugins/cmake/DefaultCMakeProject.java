package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.SetProperty;
import org.gradle.nativeplatform.TargetMachine;

import javax.inject.Inject;
import java.util.Set;

public class DefaultCMakeProject implements CMakeProject {


    private final String name;
    private final DirectoryProperty sourceDir;
    private final SetProperty<String> cmakeArgs;
    private final Property<String> baseName;
    private final SetProperty<TargetMachine> targetMachines;
    private final DefaultDomainObjectSet<SoftwareComponent> variants;

    @Inject
    public DefaultCMakeProject(String name, ObjectFactory objectFactory, CollectionCallbackActionDecorator decorator) {
        this.name = name;
        sourceDir = objectFactory.directoryProperty();
        cmakeArgs = objectFactory.setProperty(String.class);
        baseName = objectFactory.property(String.class);
        targetMachines = objectFactory.setProperty(TargetMachine.class);
        variants = new DefaultDomainObjectSet<SoftwareComponent>(SoftwareComponent.class, decorator);
    }

    @Override
    public DirectoryProperty getSourceDir() {
        return sourceDir;
    }

    @Override
    public SetProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<? extends SoftwareComponent> getVariants() {
        return variants;
    }

    @Override
    public SetProperty<TargetMachine> getTargetMachines() {
        return targetMachines;
    }

    @Override
    public Property<String> getBaseName() {
        return baseName;
    }

    @Override
    public ComponentWithVariants getMainPublication() {
        return this;
    }


}
