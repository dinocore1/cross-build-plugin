package com.devsmart.crossbuild;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.Project;
import org.gradle.api.artifacts.*;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.capabilities.Capability;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.internal.component.UsageContext;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableSet;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Set;

public class DefaultPublishableComponent implements PublishableComponent, SoftwareComponentInternal {

    public static class Builder {
        private final Project mProject;
        private String mGroup;
        private String mProjectName;
        private String mVersion;
        private BuildTarget mBuildTarget;
        private String mVariantName;

        public Builder(Project project) {
            mProject = project;
        }

        public Builder withGroup(Object group) {
            mGroup = group.toString();
            return this;
        }

        public Builder withProjectName(String projectName) {
            mProjectName = projectName;
            return this;
        }

        public Builder withVersion(Object version) {
            mVersion = version.toString();
            return this;
        }

        public Builder withBuildTarget(BuildTarget target) {
            mBuildTarget = target;
            return this;
        }

        public Builder withVariantName(String name) {
            mVariantName = name;
            return this;
        }

        public DefaultPublishableComponent build() {
            assert mProject != null;
            assert mGroup != null;
            assert mProjectName != null;
            assert mVersion != null;
            assert mVariantName != null;

            DefaultPublishableComponent retval = new DefaultPublishableComponent();
            //retval.mName = mVariantName + "_" + mBuildTarget.os + "_" + mBuildTarget.arch;
            retval.mName = mVariantName;
            retval.mModuleId = DefaultModuleVersionIdentifier.newId(mGroup, mProjectName + "_" + retval.mName, mVersion);
            retval.mUsages = ImmutableSet.of(
                    DefaultUsageContext.createLinkUsage(mProject, mVariantName),
                    DefaultUsageContext.createRuntimeUsage(mProject, mVariantName)
            );

            return retval;
        }


    }

    private static class DefaultUsageContext implements UsageContext {

        private String mName;
        private Usage mUsage;
        private AttributeContainer mAttributes;
        private PublishArtifactSet mArtifacts;
        private DomainObjectSet<ModuleDependency> mDependencies;
        private DependencyConstraintSet mDependencyConstraints;
        private Set<ExcludeRule> mGlobalExcludes;

        public static DefaultUsageContext createLinkUsage(Project project, String variantName) {
            DefaultUsageContext retval = new DefaultUsageContext();

            retval.mName = variantName + "Link";
            retval.mUsage = project.getObjects().named(Usage.class, Usage.NATIVE_LINK);
            Configuration configuration = project.getConfigurations().getByName("link" + StringUtils.capitalize(variantName));
            retval.mAttributes = configuration.getAttributes();
            retval.mArtifacts = configuration.getArtifacts();
            retval.mDependencies = configuration.getAllDependencies().withType(ModuleDependency.class);
            retval.mDependencyConstraints = configuration.getAllDependencyConstraints();
            retval.mGlobalExcludes = configuration.getExcludeRules();

            return retval;
        }

        public static DefaultUsageContext createRuntimeUsage(Project project, String variantName) {
            DefaultUsageContext retval = new DefaultUsageContext();

            retval.mName = variantName + "Runtime";
            retval.mUsage = project.getObjects().named(Usage.class, Usage.NATIVE_RUNTIME);
            Configuration configuration = project.getConfigurations().getByName("runtime" + StringUtils.capitalize(variantName));
            retval.mAttributes = configuration.getAttributes();
            retval.mArtifacts = configuration.getArtifacts();
            retval.mDependencies = configuration.getAllDependencies().withType(ModuleDependency.class);
            retval.mDependencyConstraints = configuration.getAllDependencyConstraints();
            retval.mGlobalExcludes = configuration.getExcludeRules();

            return retval;
        }

        @Override
        public Usage getUsage() {
            return mUsage;
        }

        @Override
        public Set<? extends PublishArtifact> getArtifacts() {
            return mArtifacts;
        }

        @Override
        public Set<? extends ModuleDependency> getDependencies() {
            return mDependencies;
        }

        @Override
        public Set<? extends DependencyConstraint> getDependencyConstraints() {
            return mDependencyConstraints;
        }

        @Override
        public Set<? extends Capability> getCapabilities() {
            return Collections.emptySet();
        }

        @Override
        public Set<ExcludeRule> getGlobalExcludes() {
            return mGlobalExcludes;
        }

        @Override
        public String getName() {
            return mName;
        }

        @Override
        public AttributeContainer getAttributes() {
            return mAttributes;
        }
    }

    private String mName;
    private ModuleVersionIdentifier mModuleId;
    private Set<? extends UsageContext> mUsages;

    private DefaultPublishableComponent() {
    }

    @Override
    public ModuleVersionIdentifier getCoordinates() {
        return mModuleId;
    }

    @Override
    public Set<? extends UsageContext> getUsages() {
        return mUsages;
    }

    @Override
    public String getName() {
        return mName;
    }
}
