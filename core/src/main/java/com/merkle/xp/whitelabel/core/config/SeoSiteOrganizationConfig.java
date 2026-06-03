package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration holding information about the SEO Site Organization 
 * This configuration is meant to be used as a context-aware resource. See <a href=
 * "https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware
 * Resources</a>
 */
public @interface SeoSiteOrganizationConfig {

  @Property(label = "Organization Name")
  String name() default StringUtils.EMPTY;

  @Property(label = "Organization Url")
  String url() default StringUtils.EMPTY;

  @Property(label = "Organization Logo", description = "Please only select valid, published assets in the PNG, JPEG and SVG formats.",
      property = {"widgetType=pathbrowser", "pathbrowserRootPath=/content/dam"})
  String logo();
 
  @Property(label = "Organization Telephone country code", description = "Please add the country code with the '+' suffix if necessary")
  String countryCode() default StringUtils.EMPTY;

  @Property(label = "Organization telephone", description = "A '-' will be automatically added between the country code and the telephone number")
  String telephone() default StringUtils.EMPTY;
  
  @Property(label = "Organization Contact Points", description = "Click 'Add' to add contact point")
  SeoSiteContactPointConfig[] contactPoint();
  
  @Property(label = "Social channels", description = "Click 'Add Field' to add social channel")
  String[] sameAs(); 

  @Property(label = "Json LD", description = "Add a configuration for a ld+json")
  String jsonLD();
}
