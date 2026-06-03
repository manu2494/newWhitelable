package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration holding information about the Head Scripts
 * This configuration is meant to be used as a context-aware resource. See <a href=
 * "https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware
 * Resources</a>
 */
public @interface HeadScriptsOrganizationConfig {
    @Property(label = "Scripts", description = "Enter scripts to be added in <head> of the HTML document.", property = {
            "widgetType=textarea",
            "textareaRows=20"
    })
    String scriptsHead() default StringUtils.EMPTY;

}
