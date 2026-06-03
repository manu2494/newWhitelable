package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration to map which icon use with a specific mime/type
 * This configuration is meant to be used as a context-aware resource. See <a href=
 * "https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware
 * Resources</a>
 */

@Configuration(label = "Mime/type icon mapping", description = "Configure icon by mime/type")
public @interface MimeTypeIconMapConfig {

    @Property(label = "Mime/type", description = "Choose mime/type")
    String mimeType() default StringUtils.EMPTY;

    @Property(label = "Icon class", description = "Enter icon class")
    String iconClass() default StringUtils.EMPTY;

}
