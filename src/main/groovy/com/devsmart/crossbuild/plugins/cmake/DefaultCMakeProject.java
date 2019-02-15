package com.devsmart.crossbuild.plugins.cmake;

import com.devsmart.crossbuild.StaticLibrary;
import com.devsmart.crossbuild.plugins.TargetConfig;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.SetProperty;
import org.gradle.internal.impldep.com.google.common.collect.Sets;
import org.gradle.nativeplatform.TargetMachine;
import org.gradle.platform.base.Binary;
import org.gradle.util.ConfigureUtil;

import javax.inject.Inject;
import java.util.Set;

public class DefaultCMakeProject implements CMakeProject {

    private final ObjectFactory mObjectFactory;

    private final String name;
    private final DirectoryProperty sourceDir;
    private final SetProperty<String> cmakeArgs;
    private final Property<String> baseName;
    private final NamedDomainObjectContainer<SoftwareComponent> binaries;
    private final NamedDomainObjectContainer<TargetConfig> targets;

    @Inject
    public DefaultCMakeProject(String name, Project project, ObjectFactory objectFactory, CollectionCallbackActionDecorator decorator) {
        this.name = name;
        this.binaries = project.container(SoftwareComponent.class);
        this.targets = project.container(TargetConfig.class);
        mObjectFactory = objectFactory;
        sourceDir = objectFactory.directoryProperty();
        cmakeArgs = objectFactory.setProperty(String.class);
        baseName = objectFactory.property(String.class);
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
    public NamedDomainObjectContainer<SoftwareComponent> getBinaries() {
        return binaries;
    }

    @Override
    public NamedDomainObjectContainer<TargetConfig> getTargets() {
        return targets;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<? extends SoftwareComponent> getVariants() {
        return binaries;
    }

    @Override
    public Property<String> getBaseName() {
        return baseName;
    }

    @Override
    public ComponentWithVariants getMainPublication() {
        return this;
    }

    public void target(String name, Closure c) {
        final TargetConfig newTarget = mObjectFactory.newInstance(TargetConfig.class, name);
        ConfigureUtil.configure(c, newTarget);
        targets.add(newTarget);
        binaries.withType(StaticLibrary.class, new Action<StaticLibrary>() {
            @Override
            public void execute(StaticLibrary staticLibrary) {
                staticLibrary.addTarget(newTarget);
            }
        });

    }

    public void staticLib(String name, Closure c) {
        final StaticLibrary newLib = mObjectFactory.newInstance(StaticLibrary.class, name);
        ConfigureUtil.configure(c, newLib);
        binaries.add(newLib);
    }


}
