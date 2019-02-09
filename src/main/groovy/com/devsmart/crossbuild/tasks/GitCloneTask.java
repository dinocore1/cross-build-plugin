package com.devsmart.crossbuild.tasks;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;

import javax.inject.Inject;
import java.util.ArrayList;

public class GitCloneTask extends DefaultTask {

    private final DirectoryProperty mDir;
    private final Property<String> mBranch;
    private final Property<String> mUri;

    @Inject
    public GitCloneTask(ObjectFactory objectFactory) {
        mDir = objectFactory.directoryProperty();
        mBranch = objectFactory.property(String.class);
        mUri = objectFactory.property(String.class);
    }

    @Input
    public Property<String> getBranch() {
        return mBranch;
    }

    @Input
    public Property<String> getUri() {
        return mUri;
    }

    @OutputDirectory
    public DirectoryProperty getDir() {
        return mDir;
    }

    @TaskAction
    public void cloneRepo() {
        mDir.get().getAsFile().getParentFile().mkdirs();

        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.setWorkingDir(mDir.get().getAsFile().getParentFile());
                ArrayList<String> args = new ArrayList<>();
                args.add("git");
                args.add("clone");
                args.add("--depth=1");
                args.add("-b");
                args.add(mBranch.get());
                args.add(mUri.get());
                args.add(mDir.get().getAsFile().getAbsolutePath());

                execSpec.setCommandLine(args);
            }
        }).assertNormalExitValue();
    }
}
