package com.devsmart.crossbuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Usage
import org.gradle.language.cpp.CppBinary
import org.gradle.language.plugins.NativeBasePlugin

class CrossBuildPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = 'crossbuild'

    CrossBuildExtention config


    @Override
    void apply(Project project) {

        config = project.extensions.create(EXTENSION_NAME, CrossBuildExtention, project)


        /*
         * Define some configurations to present the outputs of this build
         * to other Gradle projects.
         */
        def cppApiUsage = project.objects.named(Usage.class, Usage.C_PLUS_PLUS_API)
        def linkUsage = project.objects.named(Usage.class, Usage.NATIVE_LINK)
        def runtimeUsage = project.objects.named(Usage.class, Usage.NATIVE_RUNTIME)

        project.configurations {

            implementation {
                canBeConsumed = false
                canBeResolved = false
            }

            // incoming compile time headers - this represents the headers we consume
            cppCompile {
                canBeConsumed = false
                extendsFrom implementation
                attributes.attribute(Usage.USAGE_ATTRIBUTE, cppApiUsage)
            }

            // incoming linktime libraries (i.e. static libraries) - this represents the libraries we consume
            cppLinkDebug {
                canBeConsumed = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                }
            }
            cppLinkRelease {
                canBeConsumed = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                }
            }

            // incoming runtime libraries (i.e. shared libraries) - this represents the libraries we consume
            cppRuntimeDebug {
                canBeConsumed = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                }
            }
            cppRuntimeRelease {
                canBeConsumed = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                }
            }

            // outgoing public headers - this represents the headers we expose (including transitive headers)
            headers {
                canBeResolved = false
                extendsFrom implementation
                attributes.attribute(Usage.USAGE_ATTRIBUTE, cppApiUsage)
            }

            // outgoing linktime libraries (i.e. static libraries) - this represents the libraries we expose (including transitive headers)
            linkDebug {
                canBeResolved = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                }
            }
            linkRelease {
                canBeResolved = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                }
            }

            // outgoing runtime libraries (i.e. shared libraries) - this represents the libraries we expose (including transitive headers)
            runtimeDebug {
                canBeResolved = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                }
            }
            runtimeRelease {
                canBeResolved = false
                extendsFrom implementation
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                    attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                }
            }
        }

        project.plugins.apply(NativeBasePlugin)
        project.plugins.apply('maven-publish')


    }
}
