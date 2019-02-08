package com.devsmart.crossbuild.plugins;

import org.gradle.api.Action;
import org.gradle.api.Project;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CrossBuildExtention {

    private Project project;
    public final ArrayList<TargetConfig> targets = new ArrayList<TargetConfig>();

    @Inject
    public CrossBuildExtention(Project project) {
        this.project = project;
    }

    void targets(Action<? super List<TargetConfig>> action) {
        //action.execute(targets);
    }


}
