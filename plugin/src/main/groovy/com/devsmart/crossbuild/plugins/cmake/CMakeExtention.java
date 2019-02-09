package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.Action;
import org.gradle.api.Project;

import javax.inject.Inject;

public class CMakeExtention {

    private final Project project;

    @Inject
    public CMakeExtention(Project project) {
        this.project = project;
    }

    public void staticlib(Action<? super CMakeLibraryConfig> action) {

    }
}
