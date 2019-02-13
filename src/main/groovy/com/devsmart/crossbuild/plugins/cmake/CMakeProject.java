package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.SetProperty;
import org.gradle.language.ComponentWithTargetMachines;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

public interface CMakeProject extends PublicationAwareComponent, ComponentWithVariants, ComponentWithTargetMachines {

    DirectoryProperty getSourceDir();
    SetProperty<String> getCmakeArgs();

}
