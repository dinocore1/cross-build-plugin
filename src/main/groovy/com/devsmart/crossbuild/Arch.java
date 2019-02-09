package com.devsmart.crossbuild;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public abstract class Arch implements Named {
    public static final Attribute<Arch> ARCH_ATTRIBUTE = Attribute.of("com.devsmart.crossbuild.architecture", Arch.class);

    public static final String X86 = "x86";

    public static final String X86_64 = "x86-64";

    public static final String ARMV7A = "armv7a";

    public static final String ARM64_v8A = "arm64-v8a";
}
