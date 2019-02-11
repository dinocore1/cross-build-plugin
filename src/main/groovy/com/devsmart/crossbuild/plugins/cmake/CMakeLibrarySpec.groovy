package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;

import javax.inject.Inject;

class CMakeLibrarySpec {

    final String name
    DirectoryProperty srcDir
    List<String> cmakeArgs
    DirectoryProperty exportInclude

    CMakeLibrarySpec(String name) {
        this.name = name
    }

}
