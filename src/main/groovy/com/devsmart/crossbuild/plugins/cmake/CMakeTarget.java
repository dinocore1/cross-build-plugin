package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.Task;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.nativeplatform.TargetMachine;

import javax.inject.Inject;
import java.util.List;

public abstract class CMakeTarget implements ComponentWithCMakeArgs {

    private final String name;
    private final Property<TargetMachine> machine;
    private final ListProperty<String> cmakeArgs;
    private final DirectoryProperty installDir;
    private final DirectoryProperty buildDir;
    private Task configTask;
    private Task assembleTask;
    private Task installTask;

    @Inject
    public CMakeTarget(String name, ObjectFactory objectFactory) {
        this.name = name;
        this.machine = objectFactory.property(TargetMachine.class);
        this.cmakeArgs = objectFactory.listProperty(String.class).empty();
        this.installDir = objectFactory.directoryProperty();
        this.buildDir = objectFactory.directoryProperty();
    }

    public String getName() {
        return name;
    }

    public Property<TargetMachine> getMachine() {
        return machine;
    }

    public void setMachine(TargetMachine machine) {
        this.machine.set(machine);
    }

    @Override
    public ListProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

    public void setCmakeArgs(List<String> args) {
        this.cmakeArgs.set(args);
    }

    public DirectoryProperty getInstallDir() {
        return installDir;
    }

    public DirectoryProperty getBuildDir() {
        return buildDir;
    }


    public Task getConfigTask() {
        return configTask;
    }

    public void setConfigTask(Task configCMakeTask) {
        this.configTask = configCMakeTask;
    }

    public Task getAssembleTask() {
        return assembleTask;
    }

    public void setAssembleTask(Task buildCMakeTask) {
        this.assembleTask = buildCMakeTask;
    }

    public Task getInstallTask() {
        return installTask;
    }

    public void setInstallTask(Task installCMakeTask) {
        this.installTask = installCMakeTask;
    }
}
