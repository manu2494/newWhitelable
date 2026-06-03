package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;
@Configuration(label = "Device Configuration", description = "Configuration related to the device redirects")
public @interface SiteDeviceConfig {

    @Property(label = "Devices", description = "Config for device breakpoints")
    DeviceConfig[] devices();
}