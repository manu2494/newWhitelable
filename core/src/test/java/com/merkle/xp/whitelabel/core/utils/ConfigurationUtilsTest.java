package com.merkle.xp.whitelabel.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.merkle.xp.whitelabel.core.config.ApiConfig;
import com.merkle.xp.whitelabel.core.config.GeneralConfig;
import com.merkle.xp.whitelabel.core.config.SiteDeviceConfig;
import com.merkle.xp.whitelabel.core.config.TrackingConfig;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;


@ExtendWith({MockitoExtension.class})

class ConfigurationUtilsTest {

    @Mock
    private ConfigurationResolver configResolver;

    @Mock
    private Resource resource;
    @Mock
    private ConfigurationBuilder builder;
    @Mock
    private GeneralConfig generalConfig;
    @Mock
    private TrackingConfig trackingConfig;
    @Mock
    private ApiConfig apiConfig;
    @Mock
    private SiteDeviceConfig siteDeviceConfig;

    static MockedStatic<CommonUtils> commonUtils;


    @BeforeAll
    static void before() {
    	commonUtils = mockStatic(CommonUtils.class);
    }

    @AfterAll
    static void after() {
    	commonUtils.close();
    }
    
    void setup() throws LoginException {
    	commonUtils.when(() -> CommonUtils.getServiceFromBundle(ConfigurationResolver.class)).thenReturn(configResolver);
    	when(configResolver.get(resource)).thenReturn(builder);
    }
    
    @Test
    void testConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    	try {
    		Constructor<ConfigurationUtils> constructor = ConfigurationUtils.class.getDeclaredConstructor();
    		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    		constructor.setAccessible(true);
    		constructor.newInstance();
    		fail();
     	} catch (InvocationTargetException e) {
     		assertEquals(Constants.UTILITY_CLASS,e.getTargetException().getMessage());
     	}
    }

    @Test
    void testGetGeneralConfig() throws LoginException {
    	setup();
    	when(builder.as(GeneralConfig.class)).thenReturn(generalConfig);
    	assertEquals(generalConfig,ConfigurationUtils.getGeneralConfig(resource));
    }

    @Test
    void testGetTrackingConfig() throws LoginException {
    	setup();
    	when(builder.as(TrackingConfig.class)).thenReturn(trackingConfig);
    	assertEquals(trackingConfig,ConfigurationUtils.getTrackingConfig(resource));
    }

    @Test
    void testGetApiConfig() throws LoginException {
    	setup();
    	when(builder.as(ApiConfig.class)).thenReturn(apiConfig);
    	assertEquals(apiConfig,ConfigurationUtils.getApiConfig(resource));
    }

    @Test
    void testGetSiteDeviceConfig() throws LoginException {
    	setup();
    	when(builder.as(SiteDeviceConfig.class)).thenReturn(siteDeviceConfig);
    	assertEquals(siteDeviceConfig,ConfigurationUtils.getDeviceConfig(resource));
    }
    

}
