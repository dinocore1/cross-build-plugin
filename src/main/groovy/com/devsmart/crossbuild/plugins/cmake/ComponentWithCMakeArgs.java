package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.provider.ListProperty;

public interface ComponentWithCMakeArgs extends SoftwareComponent {

    ListProperty<String> getCmakeArgs();
}
