package com.devsmart.crossbuild.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFiles;

import javax.inject.Inject;

public class ConfigCMakeTask extends DefaultTask {


    private final ListProperty<String> mCMakeArgs;
    private final DirectoryProperty mSrcDir;

    @Inject
    public ConfigCMakeTask(ObjectFactory objectFactory) {
        mCMakeArgs = objectFactory.listProperty(String.class);
        mSrcDir = objectFactory.directoryProperty();
    }

    @Input
    public ListProperty<String> getCmakeArgs() {
        return mCMakeArgs;
    }

    @InputDirectory
    public DirectoryProperty getSrcDir() {
        return mSrcDir;
    }

    @InputFiles
    public FileCollection getCmakeLists() {
        ConfigurableFileTree retval = getProject().fileTree(mSrcDir);
        retval.include("**/CMakeLists.txt");
        return retval;
    }
}
