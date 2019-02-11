package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.artifacts.*;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.capabilities.Capability;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.api.provider.Provider;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableSet;
import org.gradle.language.cpp.internal.DefaultUsageContext;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainCMakeLibraryComponent implements ComponentWithVariants, PublicationAwareComponent, SoftwareComponentInternal {

    private final DomainObjectSet<SoftwareComponent> mVariants = new DefaultDomainObjectSet<SoftwareComponent>(SoftwareComponent.class);
    private final Set<PublishArtifact> mArtifacts = new LinkedHashSet<PublishArtifact>();
    Provider<String> baseName;
    Usage usage;
    Configuration dependencies;

    @Override
    public Set<? extends SoftwareComponent> getVariants() {
        return mVariants;
    }

    public void addVariant(SoftwareComponent variant) {
        mVariants.add(variant);
    }

    @Override
    public String getName() {
        return "main";
    }

    @Override
    public Provider<String> getBaseName() {
        return baseName;
    }

    @Override
    public ComponentWithVariants getMainPublication() {
        return this;
    }

    public void addArtifact(PublishArtifact artifact) {
        mArtifacts.add(artifact);
    }

    @Override
    public Set<? extends UsageContext> getUsages() {
        return ImmutableSet.of(new MyUsageContext());
    }

    private class MyUsageContext implements UsageContext {

        @Override
        public Usage getUsage() {
            return usage;
        }

        @Override
        public Set<? extends PublishArtifact> getArtifacts() {
            return mArtifacts;
        }

        @Override
        public Set<? extends ModuleDependency> getDependencies() {
            return dependencies.getAllDependencies().withType(ModuleDependency.class);
        }

        @Override
        public Set<? extends DependencyConstraint> getDependencyConstraints() {
            return dependencies.getAllDependencyConstraints();
        }

        @Override
        public Set<? extends Capability> getCapabilities() {
            return Collections.emptySet();
        }

        @Override
        public Set<ExcludeRule> getGlobalExcludes() {
            return dependencies.getExcludeRules();
        }

        @Override
        public String getName() {
            return "main";
        }

        @Override
        public AttributeContainer getAttributes() {
            return dependencies.getAttributes();
        }
    }
}
