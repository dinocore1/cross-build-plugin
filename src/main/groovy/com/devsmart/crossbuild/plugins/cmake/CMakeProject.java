package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.SetProperty;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

public interface CMakeProject extends PublicationAwareComponent, ComponentWithVariants {

    DirectoryProperty getSourceDir();
    SetProperty<String> getCmakeArgs();

    NamedDomainObjectContainer<SoftwareComponent> getBinaries();
    NamedDomainObjectContainer<CMakeTarget> getTargets();

}
