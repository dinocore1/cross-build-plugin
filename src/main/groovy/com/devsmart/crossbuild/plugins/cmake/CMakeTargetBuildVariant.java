package com.devsmart.crossbuild.plugins.cmake;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.language.cpp.internal.DefaultUsageContext;
import org.gradle.language.nativeplatform.internal.Names;
import org.gradle.nativeplatform.TargetMachine;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static org.gradle.language.cpp.CppBinary.DEBUGGABLE_ATTRIBUTE;
import static org.gradle.language.cpp.CppBinary.OPTIMIZED_ATTRIBUTE;
import static org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE;
import static org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE;

public class CMakeTargetBuildVariant implements CMakeProject {

    private final CMakeProject parent;
    private final String name;
    private final Names names;
    private final Property<TargetMachine> machine;
    private final ListProperty<String> cmakeArgs;
    private final DirectoryProperty installDir;
    private final DirectoryProperty buildDir;
    private final Property<String> generator;
    private final DirectoryProperty exportHeaders;
    private Task configTask;
    private Task assembleTask;
    private Task installTask;
    private UsageContext apiUsageContext;
    private Configuration includePathConfiguration;

    @Inject
    public CMakeTargetBuildVariant(CMakeProject parent, String name, ObjectFactory objectFactory, ImmutableAttributesFactory attributesFactory) {
        this.parent = parent;
        this.name = name;
        this.names = Names.of(name);
        this.machine = objectFactory.property(TargetMachine.class);
        this.cmakeArgs = objectFactory.listProperty(String.class).empty();
        this.installDir = objectFactory.directoryProperty();
        this.buildDir = objectFactory.directoryProperty();
        this.generator = objectFactory.property(String.class);
        this.exportHeaders = objectFactory.directoryProperty();
    }

    public String getName() {
        return name;
    }

    @Override
    public Names getNames() {
        return names;
    }

    public UsageContext getApiUsageContext() {
        return apiUsageContext;
    }

    public void setApiUsageContext(UsageContext context) {
        this.apiUsageContext = context;
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

    public Property<String> getGenerator() {
        return generator;
    }

    public DirectoryProperty getExportHeaders() {
        return exportHeaders;
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


    @Override
    public DirectoryProperty getSourceDir() {
        return null;
    }

    @Override
    public Set<? extends UsageContext> getUsages() {
        return null;
    }

    public void setIncludePathConfiguration(Configuration includePathConfig) {
        this.includePathConfiguration = includePathConfig;
    }

    public Configuration getIncludePathConfiguration() {
        return this.includePathConfiguration;
    }
}
