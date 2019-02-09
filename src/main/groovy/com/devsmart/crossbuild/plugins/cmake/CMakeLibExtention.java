package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;

import javax.inject.Inject;
import java.util.List;

public class CMakeLibExtention {

    private final Project project;
    private final DirectoryProperty mSrcDir;
    private final DirectoryProperty mExportInclude;
    private final ListProperty<String> mCMakeArgs;

    @Inject
    public CMakeLibExtention(Project project) {
        this.project = project;
        mSrcDir = project.getObjects().directoryProperty();
        mExportInclude = project.getObjects().directoryProperty();
        mCMakeArgs = project.getObjects().listProperty(String.class);
    }

    public DirectoryProperty getSrcDir() {
        return mSrcDir;
    }

    public DirectoryProperty getExportInclude() {
        return mExportInclude;
    }

    public ListProperty<String> getCmakeArgs() {
        return mCMakeArgs;
    }

    public void setCmakeArgs(List<String> args) {
        mCMakeArgs.set(args);
    }
}
