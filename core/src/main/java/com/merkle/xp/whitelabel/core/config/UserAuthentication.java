package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;
@Configuration(label = "Login Authorisation", description = "Configuration related to user authorisation")
public @interface UserAuthentication {
    @Property(label = "Cookies", description = "Cookies to use for authentication")
    String[] cookies();

}