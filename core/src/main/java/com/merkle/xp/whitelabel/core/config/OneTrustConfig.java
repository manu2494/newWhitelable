package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "One Trust Configuration", description = "Configuration related to One Trust")
public @interface OneTrustConfig {

    @Property(label = "One Trust Source Url", description = "One Trust Config script source url")
    String oneTrustSrcUrl();

    @Property(label = "One Trust SDK Url", description ="One Trust Config SDK url")
    String oneTrustSDKUrl() default "https://cdn.cookielaw.org/scripttemplates/otSDKStub.js";

    @Property(label = "One Trust Id", description = "One Trust Config ID")
    String oneTrustId();
}