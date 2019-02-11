package com.devsmart.crossbuild.plugins;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;

import javax.inject.Inject;

public class CrossBuildExtention {


    private final NamedDomainObjectContainer<TargetConfig> mTargets;

    @Inject
    public CrossBuildExtention(Project project) {
        mTargets = project.container(TargetConfig.class);
    }

    public NamedDomainObjectContainer<TargetConfig> getTargets() {
        return mTargets;
    }

    public void targets(Closure c) {
        mTargets.configure(c);
    }


}
