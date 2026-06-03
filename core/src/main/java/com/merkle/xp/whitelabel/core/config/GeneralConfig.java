package com.merkle.xp.whitelabel.core.config;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration holding information about the website
 * This configuration is meant to be used as a context-aware resource.
 * See <a href="https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware Resources</a>
 */
@Configuration(label = "General Configuration", description = "All configuration related to the website")
public @interface GeneralConfig {
    @Property(label = "Website Name", description = "This name is used in the opengraph metadata")
    String websiteName() default StringUtils.EMPTY;

    @Property(label = "Home Page Path", description = "Home Page Path", property = {
            "widgetType=pathbrowser",
            "pathbrowserRootPath=/content"
        })
    String homePagePath();

    @Property(label = "HREFLang x-default root", description = "Choose root for x-default tag of HREFLang. Leave empty to avoid displaying x-default", property = {
            "widgetType=pathbrowser",
            "pathbrowserRootPath=/content"
    })
    String hreflangXDefault();

    @Property(label = "Body Class", description = "Body Class Field")
    String body();
   
    @Property(label = "FavIcon", description = "Specify the image used for the FavIcon", property = {
        "widgetType=pathbrowser",
        "pathbrowserRootPath=/content/dam"
    })
    String favIcon();
    
    @Property(label = "Seo Site Organization", description = "Configuration for Seo Site Organization")
    SeoSiteOrganizationConfig seoSiteOrganization();
    
    @Property(label = "Seo Site Link", description = "Configuration for Seo Site Link")
    SeoSiteLinkConfig seoSiteLinkConfig();

    @Property(label = "Head Scripts Organization", description = "Configuration for Head Scripts")
    HeadScriptsOrganizationConfig headScriptsOrganizationConfig();

    @Property(label = "File type icon mapper", description = "Specify icons used for a specific file type")
    FileTypeIconMapConfig fileTypeIconMapping();

}
