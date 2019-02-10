package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.Project;
import org.gradle.api.attributes.Usage;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Provider;
import org.gradle.language.cpp.internal.MainLibraryVariant;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

public class CMakeLibExtention extends MainLibraryVariant implements PublicationAwareComponent {

    private final DirectoryProperty mSrcDir;
    private final DirectoryProperty mInstallDir;
    private final DirectoryProperty mExportInclude;
    private final ListProperty<String> mCMakeArgs;
    private final Provider<String> mBaseNameProvider;


    @Inject
    public CMakeLibExtention(Project project) {
        super("main", project.getObjects().named(Usage.class, Usage.C_PLUS_PLUS_API), project.getConfigurations().getByName("headers"));
        mSrcDir = project.getObjects().directoryProperty();
        mInstallDir = project.getObjects().directoryProperty();
        mExportInclude = project.getObjects().directoryProperty();
        mCMakeArgs = project.getObjects().listProperty(String.class);

        mBaseNameProvider = project.getProviders().provider(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return project.getName();
            }
        });
    }

    public DirectoryProperty getSrcDir() {
        return mSrcDir;
    }

    public DirectoryProperty getInstallDir() {
        return mInstallDir;
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


    @Override
    public Provider<String> getBaseName() {
        return mBaseNameProvider;
    }

    @Override
    public ComponentWithVariants getMainPublication() {
        return this;
    }
}
