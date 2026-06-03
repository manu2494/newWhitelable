package com.merkle.xp.whitelabel.core.utils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;

import com.merkle.xp.whitelabel.core.config.ApiConfig;
import com.merkle.xp.whitelabel.core.config.GeneralConfig;
import com.merkle.xp.whitelabel.core.config.SiteDeviceConfig;
import com.merkle.xp.whitelabel.core.config.TrackingConfig;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;

public class ConfigurationUtils {

    private ConfigurationUtils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    private static ConfigurationResolver getConfigurationResolver() {
        return CommonUtils.getServiceFromBundle(ConfigurationResolver.class);
    }

    public static <T> T getConfigByClass(Class<T> configType, Resource resource) {
        ConfigurationResolver configurationResolver = getConfigurationResolver();
        ConfigurationBuilder builder = configurationResolver.get(resource);
        T config = builder.as(configType);
        return config;
    }

    public static GeneralConfig getGeneralConfig(Resource resource) {
        return getConfigByClass(GeneralConfig.class, resource);
    }

    public static TrackingConfig getTrackingConfig(Resource resource) {
        return getConfigByClass(TrackingConfig.class, resource);
    }

    public static ApiConfig getApiConfig(Resource resource) {
        return getConfigByClass(ApiConfig.class, resource);
    }

	public static SiteDeviceConfig getDeviceConfig(Resource resource) {
        return getConfigByClass(SiteDeviceConfig.class, resource);
	}
}
