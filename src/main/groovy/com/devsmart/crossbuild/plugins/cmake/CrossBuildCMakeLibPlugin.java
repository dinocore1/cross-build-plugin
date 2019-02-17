package com.devsmart.crossbuild.plugins.cmake;

import com.devsmart.crossbuild.plugins.CrossBuildPlugin;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.component.SoftwareComponentContainer;
import org.gradle.api.file.ProjectLayout;
import org.gradle.language.internal.NativeComponentFactory;

import javax.inject.Inject;

class CrossBuildCMakeLibPlugin implements Plugin<Project> {


    private final NativeComponentFactory componentFactory;
    private final ProjectLayout layout;

    @Inject
    public CrossBuildCMakeLibPlugin(NativeComponentFactory componentFactory, ProjectLayout layout) {
        this.componentFactory = componentFactory;
        this.layout = layout;
    }


    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(CrossBuildPlugin.class);

        //final DefaultCMakeProject cmakeProject = componentFactory.newInstance(CMakeProject.class, DefaultCMakeProject.class, "main");
        final DefaultCMakeProject cmakeProject = project.getObjects().newInstance(DefaultCMakeProject.class, "main", project);
        project.getExtensions().add(CMakeProject.class, "cmake", cmakeProject);
        project.getComponents().add(cmakeProject);

        //addTargetMachinesToCMakeProject(project, project.getComponents());

        // Configure the component
        cmakeProject.getBaseName().convention(project.getName());
        cmakeProject.getSourceDir().convention(layout.getProjectDirectory().dir("src"));

    }

    private void addTargetMachinesToCMakeProject(final Project project, final SoftwareComponentContainer components) {

        NamedDomainObjectContainer<CMakeTarget> buildTargets = (NamedDomainObjectContainer<CMakeTarget>) project.getExtensions().findByName("targets");

        components.withType(CMakeProject.class, component -> {
            buildTargets.all(new Action<CMakeTarget>() {
                @Override
                public void execute(CMakeTarget targetConfig) {
                    //component.getTargetMachines().add(targetConfig.getMachine());
                    //component.getCmakeArgs().addAll(targetConfig.getCmakeArgs());
                }
            });
        });


    }




}
