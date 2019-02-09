package com.devsmart.crossbuild;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public abstract class OS implements Named {
    public static final Attribute<OS> OS_ATTRIBUTE = Attribute.of("com.devsmart.crossbuild.os", OS.class);

    public static final String WINDOWS = "windows";

    public static final String MACOS = "macos";

    public static final String LINUX = "linux";

    public static final String ANDROID = "android";

}
