package com.merkle.xp.whitelabel.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.merkle.xp.whitelabel.core.utils.constants.Constants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})

class CommonUtilsTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    Bundle bundle;

    @Mock
    BundleContext bundleContext;

    @Mock
    ResourceResolverFactory resourceResolverFactory;

    @Mock
    ServiceReference serviceReference;

    @Mock
    ResourceResolver resolver;

    @Mock
    Resource resource;

    @Mock
    Session session;

    @Mock
    Workspace workspace;

    @Captor
    ArgumentCaptor<String> sourceCaptor;

    @Captor
    ArgumentCaptor<String> destCaptor;

    static MockedStatic<FrameworkUtil> frameworkUtilMockedStatic;


    @BeforeAll
    static void before() {
        frameworkUtilMockedStatic = mockStatic(FrameworkUtil.class);
    }

    @AfterAll
    static void after() {
        frameworkUtilMockedStatic.close();
    }

    
    @Test
    void testConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    	try {
    		Constructor<CommonUtils> constructor = CommonUtils.class.getDeclaredConstructor();
    		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    		constructor.setAccessible(true);
    		constructor.newInstance();
    		fail();
     	} catch (InvocationTargetException e) {
     		assertEquals(Constants.UTILITY_CLASS,e.getTargetException().getMessage());
     	}
    }

}
