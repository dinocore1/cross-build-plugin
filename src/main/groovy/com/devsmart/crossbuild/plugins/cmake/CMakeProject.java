package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.provider.SetProperty;
import org.gradle.language.nativeplatform.internal.ComponentWithNames;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

public interface CMakeProject extends PublicationAwareComponent, ComponentWithVariants, ComponentWithNames, SoftwareComponentInternal {

    DirectoryProperty getSourceDir();
    SetProperty<String> getCmakeArgs();

    NamedDomainObjectContainer<CMakeTarget> getTargets();

}
