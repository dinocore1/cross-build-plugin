package com.devsmart.crossbuild.plugins

class TargetConfig {

    String name
    List<String> cmakeArgs = []
    String os
    String arch

    TargetConfig(String name) {
        this.name = name
    }

}
