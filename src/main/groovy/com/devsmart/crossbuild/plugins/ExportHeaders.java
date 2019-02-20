package com.devsmart.crossbuild.plugins;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.component.ComponentWithCoordinates;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.util.GUtil;

import javax.inject.Inject;
import java.util.Set;

public class ExportHeaders implements PublishableComponent, ComponentWithCoordinates, SoftwareComponentInternal {

    private final String name;

    private final DirectoryProperty includeDir;

    private final Property<Configuration> compileElements;
    private final Provider<String> baseName;
    private final Provider<String> group;
    private final Provider<String> version;
    private Zip zipHeadersTask;


    @Inject
    public ExportHeaders(String name, Provider<String> baseName, Provider<String> group, Provider<String> version, ObjectFactory objectFactory) {
        this.name = name;
        this.baseName = baseName;
        this.group = group;
        this.version = version;
        this.includeDir = objectFactory.directoryProperty();
        this.compileElements = objectFactory.property(Configuration.class);
    }

    @Override
    public String getName() {
        return name;
    }

    public DirectoryProperty getIncludeDir() {
        return includeDir;
    }

    public Zip getTask() {
        return zipHeadersTask;
    }

    public void setTask(Zip zipHeaders) {
        this.zipHeadersTask = zipHeaders;
    }


    @Override
    public ModuleVersionIdentifier getCoordinates() {
        return DefaultModuleVersionIdentifier.newId(group.get(), baseName.get() + "_" + GUtil.toWords(name, '_'), version.get());
    }


    @Override
    public Set<? extends UsageContext> getUsages() {
        return null;
    }
}
