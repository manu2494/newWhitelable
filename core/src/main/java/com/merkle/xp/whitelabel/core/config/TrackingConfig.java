package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Analytics Tracking Configuration", description = "Context-Aware Configuration for Tracking Analytics")
public @interface TrackingConfig {
    @Property(label = "Root path to config Datalayer", description = "to define where to store master config datalayer", property = {"widgetType=pathbrowser", "pathbrowserRootPath=/content"})
    String rootPath();

    @Property(label = "Variable name for window.VARIABLE_NAME")
    String variableName();

    @Property(label = "Use array instead of object", description = "Click to use array instead of object for datalayer")
    boolean useArray() default false;
    
    @Property(label = "Google Analytics UA code", description = "Google Analytics UA code")
    String googleAnalyticsUACode();

    @Property(label = "Adobe Launch Id", description = "CA config for Adobe analytics Id")
    String launchID();

    @Property(label = "Adobe Launch Property Id", description = "CA config for Adobe Launch property ID")
    String launchPropertyID();
}
