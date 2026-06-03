package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration to map which icon use with a specific file format
 * This configuration is meant to be used as a context-aware resource. See <a href=
 * "https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware
 * Resources</a>
 */
@Configuration(label = "File type icon mapping", description = "Configure icon by file type")
public @interface FileTypeIconMapConfig {

    @Property(label = "Image icon mapper", description = "Enter icon class used for image type")
    String imageIcon() default StringUtils.EMPTY;

    @Property(label = "Video icon mapper", description = "Enter icon class used for video type")
    String videoIcon() default StringUtils.EMPTY;

    @Property(label = "Document icon mapper", description = "Enter icon class used for document type")
    String documentIcon() default StringUtils.EMPTY;

    @Property(label = "Specific mime/type icon mapper", description = "Specify icons used for a specific mime/type")
    MimeTypeIconMapConfig[] mimeTypeIconMapConfig();

}
