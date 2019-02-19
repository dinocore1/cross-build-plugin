package com.devsmart.crossbuild.plugins.cmake;

import com.devsmart.crossbuild.plugins.ExportHeaders;
import com.devsmart.crossbuild.plugins.StaticLibrary;
import com.devsmart.crossbuild.tasks.BuildCMakeTask;
import com.devsmart.crossbuild.tasks.ConfigCMakeTask;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.internal.impldep.com.google.common.collect.Sets;
import org.gradle.language.cpp.internal.DefaultUsageContext;
import org.gradle.language.cpp.internal.NativeVariantIdentity;
import org.gradle.language.internal.DefaultComponentDependencies;
import org.gradle.language.internal.DefaultLibraryDependencies;
import org.gradle.language.nativeplatform.internal.Names;
import org.gradle.nativeplatform.TargetMachine;
import org.gradle.util.ConfigureUtil;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.Callable;

import static org.gradle.language.cpp.CppBinary.LINKAGE_ATTRIBUTE;

public class DefaultCMakeProject implements CMakeProject {

    private final ObjectFactory mObjectFactory;

    private final String name;
    private final DirectoryProperty sourceDir;
    private final SetProperty<String> cmakeArgs;
    private final Property<String> baseName;
    private final DefaultDomainObjectSet<SoftwareComponent> binaries;
    private final NamedDomainObjectContainer<CMakeTarget> targets;
    private final Project project;
    private final Provider<String> groupName;
    private final Provider<String> versionName;
    private final Names names;
    private final DefaultLibraryDependencies dependencies;
    private final Configuration apiElements;

    @Inject
    public DefaultCMakeProject(String name, Project project, ObjectFactory objectFactory, CollectionCallbackActionDecorator decorator) {
        this.name = name;
        this.binaries = new DefaultDomainObjectSet<SoftwareComponent>(SoftwareComponent.class, decorator);
        this.targets = project.container(CMakeTarget.class);
        this.project = project;
        this.names = Names.of(name);
        mObjectFactory = objectFactory;
        sourceDir = objectFactory.directoryProperty();
        cmakeArgs = objectFactory.setProperty(String.class);
        baseName = objectFactory.property(String.class);
        groupName = project.provider(new Callable<String>(){

            @Override
            public String call() throws Exception {
                return (String) project.getGroup();
            }
        });

        versionName = project.provider(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return (String) project.getVersion();
            }
        });

        dependencies = objectFactory.newInstance(DefaultLibraryDependencies.class, getNames().withSuffix("implementation"), getNames().withSuffix("api"));
        Usage apiUsage = objectFactory.named(Usage.class, Usage.C_PLUS_PLUS_API);
        apiElements = project.getConfigurations().create(getNames().withSuffix("cppApiElements"));
        apiElements.extendsFrom(dependencies.getApiDependencies());
        apiElements.setCanBeResolved(false);
        apiElements.getAttributes().attribute(Usage.USAGE_ATTRIBUTE, apiUsage);

    }

    @Override
    public Names getNames() {
        return names;
    }

    @Override
    public DirectoryProperty getSourceDir() {
        return sourceDir;
    }

    @Override
    public SetProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

    @Override
    public NamedDomainObjectContainer<CMakeTarget> getTargets() {
        return targets;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<? extends SoftwareComponent> getVariants() {
        return binaries;
    }

    @Override
    public Property<String> getBaseName() {
        return baseName;
    }

    @Override
    public ComponentWithVariants getMainPublication() {
        return this;
    }

    public CMakeTarget target(String name, Closure c) {
        final CMakeTarget newTarget = mObjectFactory.newInstance(CMakeTarget.class, name, project.container(SoftwareComponent.class));
        ConfigureUtil.configure(c, newTarget);

        TargetMachine machine = newTarget.getMachine().get();
        Names targetName = Names.of(machine.getOperatingSystemFamily().getName()).append(machine.getArchitecture().getName());

        DirectoryProperty targetBuildDir = mObjectFactory.directoryProperty();
        targetBuildDir.set(project.getLayout().getBuildDirectory().dir(targetName.getDirName()));

        newTarget.getBuildDir().set(targetBuildDir.dir("build"));
        newTarget.getInstallDir().set(targetBuildDir.dir("install"));

        final ConfigCMakeTask configTask = project.getTasks().create(targetName.getTaskName("config"), ConfigCMakeTask.class, task -> {
            task.getSrcDir().set(sourceDir);
            task.getBuildDir().set(newTarget.getBuildDir());
            task.getInstallDir().set(newTarget.getInstallDir());
            task.getCmakeArgs().addAll(cmakeArgs);
            task.getCmakeArgs().addAll(newTarget.getCmakeArgs());
            task.getCmakeArgs().add(project.provider( () -> {
                return "CMAKE_INSTALL_PREFIX=" + task.getInstallDir().getAsFile().get().getAbsolutePath();
            }));

            task.getGenerator().set(newTarget.getGenerator());

        });
        newTarget.setConfigTask(configTask);

        final BuildCMakeTask assembleTask = project.getTasks().create(targetName.getTaskName("assemble"), BuildCMakeTask.class, task -> {
            task.generatedBy(configTask);
            task.getBuildOutputs().convention(project.fileTree(configTask.getBuildDir()));

        });
        newTarget.setAssembleTask(assembleTask);

        final BuildCMakeTask installTask = project.getTasks().create(targetName.getTaskName("install"), BuildCMakeTask.class, task -> {
            task.generatedBy(configTask);
            task.getTarget().set("install");
            task.getBuildOutputs().convention(project.fileTree(configTask.getInstallDir()));
            task.dependsOn(assembleTask);
        });
        newTarget.setInstallTask(installTask);

        if(newTarget.getExportHeaders().isPresent()) {

            Names headerName = Names.of(targetName.withPrefix("headers"));

            NativeVariantIdentity id = new NativeVariantIdentity(headerName.getName(), baseName, groupName, versionName, false, false, machine, null, null);

            ExportHeaders exportHeaders = mObjectFactory.newInstance(ExportHeaders.class, headerName, id, getImplementationDependencies());
            exportHeaders.getIncludeDir().set(newTarget.getExportHeaders());
            exportHeaders.setTask(project.getTasks().create(headerName.getTaskName("zip"), Zip.class, task -> {
                task.from(exportHeaders.getIncludeDir());
                task.getDestinationDirectory().set(targetBuildDir);
                task.getArchiveBaseName().set(getBaseName().get());
                task.getArchiveClassifier().set("api-headers");
                task.dependsOn(installTask);

            }));

            binaries.add(exportHeaders);
            project.getComponents().add(exportHeaders);

            newTarget.getBinaries().add(exportHeaders);

        }

        binaries.withType(CMakeStaticLibrary.class, new Action<CMakeStaticLibrary>() {
            @Override
            public void execute(CMakeStaticLibrary staticLibrary) {

                NativeVariantIdentity id = new NativeVariantIdentity(targetName.getName(), baseName, groupName, versionName, false, false, machine, null, null);


                StaticLibrary newLib = mObjectFactory.newInstance(StaticLibrary.class, getNames().append(id.getName()), id);

            }
        });

        targets.add(newTarget);
        return newTarget;

    }

    public CMakeStaticLibrary staticLib(String name, Closure c) {
        final CMakeStaticLibrary newLib = mObjectFactory.newInstance(CMakeStaticLibrary.class, name, project.container(SoftwareComponent.class));
        ConfigureUtil.configure(c, newLib);
        binaries.add(newLib);
        return newLib;
    }

    public Configuration getImplementationDependencies() {
        return dependencies.getImplementationDependencies();
    }

    private ModuleVersionIdentifier createModuleVersionId(final Provider<String> name) {
        final ModuleIdentifier id = DefaultModuleIdentifier.newId((String) project.getGroup(), project.getName());

        return new ModuleVersionIdentifier() {
            @Override
            public String getVersion() {
                return (String) project.getVersion();
            }

            @Override
            public String getGroup() {
                return id.getGroup();
            }

            @Override
            public String getName() {
                return name.get();
            }

            @Override
            public ModuleIdentifier getModule() {
                return id;
            }
        };
    }


    @Override
    public Set<? extends UsageContext> getUsages() {
        return null;
    }
}
