package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.provider.Provider;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

import java.util.Set;

public class MainCMakeLibraryComponent implements ComponentWithVariants, PublicationAwareComponent {

    private final DomainObjectSet<SoftwareComponent> mVariants = new DefaultDomainObjectSet<SoftwareComponent>(SoftwareComponent.class);
    Provider<String> baseName;

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
}
