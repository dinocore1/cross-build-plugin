package com.devsmart.crossbuild.plugins;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.provider.Property;
import org.gradle.language.nativeplatform.internal.ComponentWithNames;

public interface ConfigurableComponentWithCompileUsage extends ComponentWithNames, SoftwareComponent {

    Configuration getImplementationDependencies();

    Property<Configuration> getCompileElements();

    AttributeContainer getCompileAttributes();
}
