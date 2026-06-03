package com.merkle.xp.whitelabel.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.merkle.xp.whitelabel.core.config.ApiConfig;
import com.merkle.xp.whitelabel.core.config.GeneralConfig;
import com.merkle.xp.whitelabel.core.config.SiteDeviceConfig;
import com.merkle.xp.whitelabel.core.config.TrackingConfig;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;


@ExtendWith({MockitoExtension.class})

class PageUtilsTest {

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
    
    void setup() throws LoginException {
    }
    
    @Test
    void testConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    	try {
    		Constructor<PageUtils> constructor = PageUtils.class.getDeclaredConstructor();
    		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    		constructor.setAccessible(true);
    		constructor.newInstance();
    		fail();
     	} catch (InvocationTargetException e) {
     		assertEquals(Constants.UTILITY_CLASS,e.getTargetException().getMessage());
     	}
    }
}
