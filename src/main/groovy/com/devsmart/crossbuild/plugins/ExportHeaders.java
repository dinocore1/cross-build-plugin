package com.devsmart.crossbuild.plugins;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.internal.impldep.com.google.common.collect.Sets;
import org.gradle.language.cpp.internal.DefaultUsageContext;

import javax.inject.Inject;
import java.util.Set;
import org.gradle.language.cpp.internal.NativeVariantIdentity;
import org.gradle.language.internal.DefaultComponentDependencies;
import org.gradle.language.nativeplatform.internal.Names;

public class ExportHeaders implements PublishableComponent, SoftwareComponentInternal, ConfigurableComponentWithCompileUsage {

    private final Names names;
    private final DirectoryProperty includeDir;
    private final NativeVariantIdentity identity;
    private final Property<Configuration> compileElements;
    private final DefaultComponentDependencies dependencies;

    private Zip zipHeadersTask;


    @Inject
    public ExportHeaders(Names names, NativeVariantIdentity identity, Configuration componentImplementation, ObjectFactory objectFactory) {
        this.names = names;
        this.identity = identity;
        this.includeDir = objectFactory.directoryProperty();
        this.compileElements = objectFactory.property(Configuration.class);
        this.dependencies = objectFactory.newInstance(DefaultComponentDependencies.class, names.getName() + "Implementation");
        this.dependencies.getImplementationDependencies().extendsFrom(componentImplementation);

    }

    @Override
    public String getName() {
        return identity.getName();
    }

    @Override
    public ModuleVersionIdentifier getCoordinates() {
        return identity.getCoordinates();
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
    public Set<? extends UsageContext> getUsages() {
        Configuration linkElements = compileElements.get();
        return Sets.newHashSet(
                new DefaultUsageContext(identity.getLinkUsageContext(), linkElements.getAllArtifacts(), linkElements)
        );
    }

    @Override
    public Configuration getImplementationDependencies() {
        return dependencies.getImplementationDependencies();
    }

    @Override
    public Property<Configuration> getCompileElements() {
        return compileElements;
    }

    @Override
    public AttributeContainer getCompileAttributes() {
        return identity.getLinkUsageContext().getAttributes();
    }

    @Override
    public Names getNames() {
        return names;
    }
}
