package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;
@Configuration(label = "API Configuration", description = "Configuration related to the External APIs")
public @interface ApiConfig {

    @Property(label = "adminApiName", description = "Name of the configuration of an admin API")
    String adminApiName();

    @Property(label = "userApiName", description = "Name of the configuration of a user API")
    String userApiName();
}