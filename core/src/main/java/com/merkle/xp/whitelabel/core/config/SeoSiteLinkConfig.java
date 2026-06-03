package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration holding information about the SEO site link data
 * This configuration is meant to be used as a context-aware resource.
 * See <a href="https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware Resources</a>
 */
public @interface SeoSiteLinkConfig {

  @Property(label = "Website URL", description = "Use the homepage URL. If left out the canonical URL will be set.")
  String url() default StringUtils.EMPTY;

  @Property(label = "Search Target", description = "Set the search page path including the search query marker at the end, for example: '/search_page_path?q='",
      property = {"widgetType=pathbrowser", "pathbrowserRootPath=/content"})
  String target();

}
