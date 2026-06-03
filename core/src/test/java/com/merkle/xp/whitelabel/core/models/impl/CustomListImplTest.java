package com.merkle.xp.whitelabel.core.models.impl;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.cq.wcm.core.components.internal.models.v3.ListImpl;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.scripting.WCMBindingsConstants;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class CustomListImplTest {

    private static final String PAGE = "/content/core-framework/us/en/page";
    private static final String COMPONENT_FIXED = PAGE + "/jcr:content/root/comp";
    private static final String COMPONENT_FIXED_NO_WRAPPED = PAGE + "/jcr:content/root/compNoWrapped";
    private static final String COMPONENT = PAGE + "/jcr:content/root/comp1";

    @Mock
    private Component component;

    @InjectMocks
    List coreList = new ListImpl();

    private final AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    public void setup() throws Exception {

        context.load().json("/pages/home-page.json", PAGE);
        context.load().json("/components/custom-list-fixed.json", COMPONENT_FIXED);
        context.load().json("/components/custom-list-fixed-nowrapped.json", COMPONENT_FIXED_NO_WRAPPED);
        context.load().json("/components/custom-list.json", COMPONENT);
        context.registerAdapter(SlingHttpServletRequest.class, Component.class, component);

        Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
        context.addModelsForClasses(Component.class);
        context.currentPage(page);

        component = context.request().adaptTo(Component.class);
        SlingBindings slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        slingBindings.put(WCMBindingsConstants.NAME_COMPONENT, component);
        context.request().setAttribute(SlingBindings.class.getName(), slingBindings);
    }

    @Test
    void test_get_comp_fixed_list() {
        Resource res = context.resourceResolver().getResource(COMPONENT_FIXED);
        context.currentResource(res);

        CustomListImpl model = context.request().adaptTo(CustomListImpl.class);

        assertNotNull(model);
        when(component.getProperties()).thenReturn(new ValueMapDecorator(new HashMap<>()));
        model.component = component;
        Collection<ListItem> listItems = model.getListItems();
        assertNotNull(listItems);

        FixedListItemImpl listItem = (FixedListItemImpl) listItems.stream().findFirst().get();
        assertNotNull(listItem.getTeaserResource());
        assertNotNull(listItem.getDescription());
        assertNotNull(listItem.getLink());
        assertNotNull(listItem.getTitle());
        assertNull(listItem.getIcon());
        assertNotNull(listItem.getLinkUrl());
        assertNotNull(listItem.getTags());
    }

    @Test
    void test_get_comp_fixed_list_nowrapped() {
        Resource res = context.resourceResolver().getResource(COMPONENT_FIXED_NO_WRAPPED);
        context.currentResource(res);

        CustomListImpl model = context.request().adaptTo(CustomListImpl.class);

        assertNotNull(model);
        when(component.getProperties()).thenReturn(new ValueMapDecorator(new HashMap<>()));
        model.component = component;
        Collection<ListItem> listItems = model.getListItems();
        assertNotNull(listItems);
        ListItem listItem = listItems.stream().findFirst().get();
        assertNull(listItem.getTeaserResource());
    }
}
