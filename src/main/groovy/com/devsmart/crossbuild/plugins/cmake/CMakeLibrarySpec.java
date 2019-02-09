package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Provider;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

public class CMakeLibrarySpec implements ComponentWithVariants {

    private final ObjectFactory objectFactory;

    private final ListProperty<String> mCMakeArgs;
    public final DirectoryProperty sourceDir;
    public final DirectoryProperty exportInclude;

    @Inject
    public CMakeLibrarySpec(ProjectLayout projectLayout, ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;

        this.sourceDir = objectFactory.directoryProperty();
        //default assumes the CMake source dir is the project dir
        this.sourceDir.set(projectLayout.getProjectDirectory());

        this.mCMakeArgs = objectFactory.listProperty(String.class);
        this.exportInclude = objectFactory.directoryProperty();
    }

    public ListProperty<String> cmakeArgs() {
        return mCMakeArgs;
    }

    public void setCmakeArgs(List<String> args) {
        mCMakeArgs.set(args);
    }


    @Override
    public Set<? extends SoftwareComponent> getVariants() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

}
