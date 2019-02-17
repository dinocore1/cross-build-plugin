package com.devsmart.crossbuild.plugins;

import com.devsmart.crossbuild.plugins.cmake.CMakeTarget;
import groovy.lang.Closure;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;

import javax.inject.Inject;

public class CrossBuildExtention {


    private final NamedDomainObjectContainer<CMakeTarget> mTargets;

    @Inject
    public CrossBuildExtention(Project project) {
        mTargets = project.container(CMakeTarget.class);
    }

    public NamedDomainObjectContainer<CMakeTarget> getTargets() {
        return mTargets;
    }

    public void targets(Closure c) {
        mTargets.configure(c);
    }


}
