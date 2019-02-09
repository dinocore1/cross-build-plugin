package com.devsmart.crossbuild.plugins;

import com.devsmart.crossbuild.Arch;
import com.devsmart.crossbuild.BuildTarget;
import com.devsmart.crossbuild.OS;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;

import javax.inject.Inject;
import java.util.List;

public class TargetConfig {

    private ObjectFactory objectFactory;
    private final ListProperty<String> mCMakeArgs;
    private OS os;
    private Arch arch;

    @Inject
    public TargetConfig(ProjectLayout projectLayout, ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
        this.mCMakeArgs = objectFactory.listProperty(String.class);
    }

    public void setOs(String osStr) {
        os = objectFactory.named(OS.class, osStr);
    }

    public void setArch(String archStr) {
        arch = objectFactory.named(Arch.class, archStr);
    }

    public BuildTarget getBuildTarget() {
        return new BuildTarget(os, arch);
    }

    public ListProperty<String> getCmakeArgs() {
        return mCMakeArgs;
    }

    public void setCMakeArgs(List<String> args) {
        mCMakeArgs.set(args);
    }

}
