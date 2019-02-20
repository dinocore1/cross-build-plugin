package com.devsmart.crossbuild.plugins;

import com.devsmart.crossbuild.plugins.cmake.CMakeTargetBuildVariant;
import groovy.lang.Closure;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;

import javax.inject.Inject;

public class CrossBuildExtention {


    private final NamedDomainObjectContainer<CMakeTargetBuildVariant> mTargets;

    @Inject
    public CrossBuildExtention(Project project) {
        mTargets = project.container(CMakeTargetBuildVariant.class);
    }

    public NamedDomainObjectContainer<CMakeTargetBuildVariant> getTargets() {
        return mTargets;
    }

    public void targets(Closure c) {
        mTargets.configure(c);
    }


}
