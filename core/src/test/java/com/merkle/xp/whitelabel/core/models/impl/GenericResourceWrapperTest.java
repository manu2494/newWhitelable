package com.merkle.xp.whitelabel.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import acscommons.com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class GenericResourceWrapperTest {

    private final AemContext context = AppAemContext.newAemContext();

    String resourceType = "core-framework/components/test-component";
    String overrideResourceType = "core-framework/components/test-override-component";

    @BeforeEach
    void setUp() {
        context.create().resource("/content/test", String.format("sling:%s", SlingConstants.PROPERTY_RESOURCE_TYPE), resourceType);

        context.currentResource(context.resourceResolver().getResource("/content/test"));
    }

    @Test
    void test_with_resource() {
        GenericResourceWrapper resourceWrapper = new GenericResourceWrapper(context.currentResource());

        assertEquals(resourceType, resourceWrapper.getResourceType());
    }

    @Test
    void test_with_resource_and_resourceType() {
        GenericResourceWrapper resourceWrapper = new GenericResourceWrapper(context.currentResource(), overrideResourceType);

        assertEquals(overrideResourceType, resourceWrapper.getResourceType());
        assertFalse(resourceWrapper.isResourceType("/resoruceType/1"));
    }

    @Test
    void test_with_resource_resourceType_and_valueMap() {
        ValueMap valueMap = new ValueMapDecorator(ImmutableMap.of("resourceType", overrideResourceType));
        GenericResourceWrapper resourceWrapper = new GenericResourceWrapper(context.currentResource(), overrideResourceType, valueMap);

        assertEquals(overrideResourceType, resourceWrapper.getValueMap().get("resourceType", String.class));
    }
}
