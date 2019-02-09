package com.devsmart.crossbuild.tasks;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.gradle.process.ExecSpec;

import javax.inject.Inject;
import java.util.ArrayList;

public class BuildCMakeTask extends DefaultTask {

    private final DirectoryProperty mBuildDir;
    private final Property<String> mTarget;
    private FileCollection mBuildOutputs;
    private FileCollection mMakefiles;

    @Inject
    public BuildCMakeTask(ObjectFactory objectFactory) {
        mBuildDir = objectFactory.directoryProperty();
        mTarget = objectFactory.property(String.class);
    }

    @TaskAction
    public void execute() {
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                String cmakeExe = System.getenv("CMAKE_EXECUTABLE");
                if(cmakeExe == null) {
                    cmakeExe = "cmake";
                }

                ArrayList<String> args = new ArrayList<>();
                args.add(cmakeExe);
                args.add("--build");
                args.add(mBuildDir.get().getAsFile().getAbsolutePath());

                if(mTarget.isPresent()) {
                    args.add("--target");
                    args.add(mTarget.get());
                }

                execSpec.setCommandLine(args);

            }
        }).assertNormalExitValue();
    }

    @InputFiles
    public FileCollection getMakeFiles() {
        return mMakefiles;
    }

    @Internal
    public DirectoryProperty getBuildDir() {
        return mBuildDir;
    }

    @Input
    @Optional
    public Property<String> getTarget() {
        return mTarget;
    }

    @OutputFiles
    public FileCollection getBuildOutputs() {
        return mBuildOutputs;
    }

    public void setBuildOutputs(FileCollection outputs) {
        mBuildOutputs = outputs;
    }


    public void generatedBy(ConfigCMakeTask configTask) {
        mBuildDir.set(configTask.getBuildDir());
        mMakefiles = configTask.getCmakeFiles();
        dependsOn(configTask);
    }
}
