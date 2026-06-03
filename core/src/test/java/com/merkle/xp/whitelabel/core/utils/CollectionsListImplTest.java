package com.merkle.xp.whitelabel.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.scripting.WCMBindingsConstants;
import com.merkle.xp.whitelabel.core.models.impl.CollectionListItemImpl;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class CollectionsListImplTest {

	private static final String COLLECTION_PAGE_PATH = "/content/digital/global/damportal/us/en/collections/collection-1";

	private final AemContext context = AppAemContext.newAemContext();

	private Resource resource;

	@Mock
	private Component component;

	@Mock
	private LinkManager linkManager;

	@BeforeEach
	void setup() throws Exception {

		context.load().json("/pages/collection-1.json", COLLECTION_PAGE_PATH);
		linkManager = context.request().adaptTo(LinkManager.class);
		resource = context.resourceResolver().getResource(COLLECTION_PAGE_PATH);
		context.registerAdapter(SlingHttpServletRequest.class, Component.class, component);
		context.addModelsForClasses(Component.class);
		context.registerAdapter(SlingHttpServletRequest.class, Component.class, component);
		component = context.request().adaptTo(Component.class);
		SlingBindings slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
		slingBindings.put(WCMBindingsConstants.NAME_COMPONENT, component);
		context.currentResource(resource);
	}

	@Test
	void testConstructor() {
		when(component.getProperties()).thenReturn(new ValueMapDecorator(new HashMap<>()));
		CollectionListItemImpl listItem = new CollectionListItemImpl(resource, linkManager, component,
				context.resourceResolver());
		assertEquals("Collection 1", listItem.getTitle());
		assertEquals("/content/digital/global/damportal/us/en/collections/collection-1.html", listItem.getLinkUrl());
		assertEquals("/content/dam/digital/global/test.png", listItem.getIcon());
		assertNull(listItem.getDescription());
		assertNull(listItem.getLastModified());
	}
}
