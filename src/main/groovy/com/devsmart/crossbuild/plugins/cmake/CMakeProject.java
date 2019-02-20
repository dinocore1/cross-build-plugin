package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.language.nativeplatform.internal.ComponentWithNames;

public interface CMakeProject extends ComponentWithCMakeArgs, ComponentWithNames, SoftwareComponentInternal {

    DirectoryProperty getSourceDir();

}
