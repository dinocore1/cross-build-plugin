package com.devsmart.crossbuild.tasks;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.process.ExecSpec;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ConfigCMakeTask extends DefaultTask {


    private final ListProperty<String> mCMakeArgs;
    private final DirectoryProperty mSrcDir;
    private final DirectoryProperty mBuildDir;
    private final Property<String> mGenerator;

    @Inject
    public ConfigCMakeTask(ObjectFactory objectFactory) {
        mCMakeArgs = objectFactory.listProperty(String.class);
        mSrcDir = objectFactory.directoryProperty();
        mBuildDir = objectFactory.directoryProperty();
        mGenerator = objectFactory.property(String.class);
    }

    @TaskAction
    void generateCMakeFiles() {

        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.setWorkingDir(mBuildDir.get());

                String cmakeExe = System.getenv("CMAKE_EXECUTABLE");
                if(cmakeExe == null) {
                    cmakeExe = "cmake";
                }

                ArrayList<String> args = new ArrayList<>();
                args.add(cmakeExe);
                if(mGenerator.isPresent()) {
                    args.add("-G");
                    args.add(mGenerator.get());
                }

                for(String arg : mCMakeArgs.get()) {
                    args.add("-D" + arg);
                }

                args.add(mSrcDir.get().getAsFile().getAbsolutePath());

                execSpec.setCommandLine(args);

            }
        }).assertNormalExitValue();

    }

    @Internal
    public DirectoryProperty getSrcDir() {
        return mSrcDir;
    }

    @Internal
    public DirectoryProperty getBuildDir() {
        return mBuildDir;
    }

    @Input
    @Optional
    public Property<String> getGenerator() {
        return mGenerator;
    }

    @Input
    public ListProperty<String> getCmakeArgs() {
        return mCMakeArgs;
    }

    public void setCmakeArgs(List<String> args) {
        mCMakeArgs.set(args);
    }

    @InputFiles
    public FileCollection getCmakeLists() {
        return getProject().fileTree(mSrcDir).matching(new PatternSet()
            .include("**/CMakeLists.txt"));
    }

    @OutputFiles
    FileCollection getCmakeFiles() {
        return getProject().fileTree(mBuildDir).matching(new PatternSet()
                .include("**/CMakeFiles/**/*")
                .include("**/Makefile")
                .include("**/*.cmake"));

    }
}
