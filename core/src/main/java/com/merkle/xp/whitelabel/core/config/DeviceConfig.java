package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

public @interface DeviceConfig {
    @Property(label = "Minimum size", description = "The device breakpoint start")
    int minSize() default 0;

    @Property(label = "Maximum size", description = "The device breakpoint end")
    int maxSize() default 768;

    @Property(label = "Redirect", description = "Configure the breakpoint redirect")
    String redirect() default StringUtils.EMPTY;
}