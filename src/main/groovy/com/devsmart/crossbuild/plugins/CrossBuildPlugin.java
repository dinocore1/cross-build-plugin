package com.devsmart.crossbuild.plugins;

import com.devsmart.crossbuild.plugins.cmake.CMakeTargetBuildVariant;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.component.ComponentWithVariants;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.component.SoftwareComponentContainer;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publisher.MutableMavenProjectIdentity;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.internal.Cast;
import org.gradle.language.nativeplatform.internal.Names;
import org.gradle.language.nativeplatform.internal.PublicationAwareComponent;
import org.gradle.nativeplatform.TargetMachineFactory;

import javax.inject.Inject;
import java.util.Set;

public class CrossBuildPlugin implements Plugin<Project> {

    private final TargetMachineFactory targetMachineFactory;

    @Inject
    public CrossBuildPlugin(TargetMachineFactory targetMachineFactory) {
        this.targetMachineFactory = targetMachineFactory;
    }

    @Override
    public void apply(Project project) {

        addTargetMachineFactoryAsExtension(project.getExtensions(), targetMachineFactory);

        final NamedDomainObjectContainer<CMakeTargetBuildVariant> targets = project.container(CMakeTargetBuildVariant.class);
        project.getExtensions().add("targets", targets);

        final TaskContainer tasks = project.getTasks();
        final DirectoryProperty buildDirectory = project.getLayout().getBuildDirectory();
        final SoftwareComponentContainer components = project.getComponents();
        final ConfigurationContainer configurations = project.getConfigurations();

        addOutgoingConfigurationForCompileUsage(components, configurations);

        addPublicationsFromVariants(project, components);
    }

    private void addOutgoingConfigurationForCompileUsage(SoftwareComponentContainer components, ConfigurationContainer configurations) {
        components.withType(ConfigurableComponentWithCompileUsage.class, component -> {
            Names names = component.getNames();
            Configuration compileElements = configurations.create(names.withSuffix("compileElements"));
            compileElements.extendsFrom(component.getImplementationDependencies());
            compileElements.setCanBeResolved(false);
            AttributeContainer attributes = component.getCompileAttributes();
            copyAttributesTo(attributes, compileElements);
            //compileElements.getOutgoing().artifact(component.getLinkFile());
            component.getCompileElements().set(compileElements);

        });

    }

    private void copyAttributesTo(AttributeContainer attributes, Configuration linkElements) {
        for (Attribute<?> attribute : attributes.keySet()) {
            Object value = attributes.getAttribute(attribute);
            linkElements.getAttributes().attribute(Cast.<Attribute<Object>>uncheckedCast(attribute), value);
        }
    }

    private static void addTargetMachineFactoryAsExtension(ExtensionContainer extensions, TargetMachineFactory targetMachineFactory) {
        extensions.add(TargetMachineFactory.class, "machines", targetMachineFactory);
    }

    private void addPublicationsFromVariants(final Project project, final SoftwareComponentContainer components) {
        project.getPluginManager().withPlugin("maven-publish", plugin -> {
            components.withType(PublicationAwareComponent.class, component -> {
                project.getExtensions().configure(PublishingExtension.class, publishing -> {
                    final ComponentWithVariants mainVariant = component.getMainPublication();
                    publishing.getPublications().create("main", MavenPublication.class, publication -> {
                        MavenPublicationInternal publicationInternal = (MavenPublicationInternal) publication;
                        publicationInternal.getMavenProjectIdentity().getArtifactId().set(component.getBaseName());
                        publicationInternal.from(mainVariant);
                        publicationInternal.publishWithOriginalFileName();
                    });

                    Set<? extends SoftwareComponent> variants = mainVariant.getVariants();
                    if (variants instanceof DomainObjectSet) {
                        ((DomainObjectSet<? extends SoftwareComponent>) variants).all(child -> addPublicationFromVariant(child, publishing, project));
                    } else {
                        for (SoftwareComponent variant : variants) {
                            addPublicationFromVariant(variant, publishing, project);
                        }
                    }
                });
            });
        });
    }

    private void addPublicationFromVariant(SoftwareComponent child, PublishingExtension publishing, Project project) {
        if (child instanceof PublishableComponent) {
            publishing.getPublications().create(child.getName(), MavenPublication.class, publication -> {
                MavenPublicationInternal publicationInternal = (MavenPublicationInternal) publication;
                fillInCoordinates(project, publicationInternal, (PublishableComponent) child);
                publicationInternal.from(child);
                publicationInternal.publishWithOriginalFileName();
            });
        }
    }

    private void fillInCoordinates(Project project, MavenPublicationInternal publication, PublishableComponent publishableComponent) {
        final ModuleVersionIdentifier coordinates = publishableComponent.getCoordinates();
        MutableMavenProjectIdentity identity = publication.getMavenProjectIdentity();
        identity.getGroupId().set(project.provider(() -> coordinates.getGroup()));
        identity.getArtifactId().set(project.provider(() -> coordinates.getName()));
        identity.getVersion().set(project.provider(() -> coordinates.getVersion()));
    }
}
