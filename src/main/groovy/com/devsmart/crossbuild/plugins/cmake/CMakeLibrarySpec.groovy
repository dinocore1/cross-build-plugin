package com.devsmart.crossbuild.plugins.cmake


import org.gradle.api.file.DirectoryProperty
import org.gradle.util.Configurable
import org.gradle.util.ConfigureUtil

class CMakeLibrarySpec implements Configurable<CMakeLibrarySpec> {

    DirectoryProperty srcDir
    List<String> cmakeArgs
    DirectoryProperty exportInclude
    MainCMakeLibraryComponent component

    Closure whenConfigured

    @Override
    CMakeLibrarySpec configure(Closure cl) {
        CMakeLibrarySpec retval = ConfigureUtil.configureSelf(cl, this)
        whenConfigured()
        return retval
    }
}
