package com.merkle.xp.whitelabel.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Maps;
import com.merkle.xp.whitelabel.core.services.GenericListService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class GenericListServiceImplTest {

	final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	GenericListService tested;

	@BeforeEach
	void setUp() {
		context.registerService(GenericListService.class, new GenericListServiceImpl());
		context.registerAdapter(Page.class, GenericList.class,
				(Function<Page, GenericList>) page -> page.getContentResource().adaptTo(GenericList.class));
		tested = context.getService(GenericListService.class);
		if (tested == null) {
			throw new NullPointerException("Tested service not available");
		}
	}

	@Test
	void testPopulateGenListMap() {
		context.load().json("/genericlist/list.json", "/etc/acs-commons/lists/webcenter-mapping/ea-cbu");
		Map<String, String> list = tested.populateGenListMap(context.resourceResolver(),
				"/etc/acs-commons/lists/webcenter-mapping/ea-cbu");
		assertNotNull(list);
		assertEquals(3, list.size());
		assertNotNull(list.get("Africa & Overseas"));
		assertEquals("africa", list.get("Africa & Overseas"));
		assertNotNull(list.get("Argentina"));
		assertEquals("arg", list.get("Argentina"));
		assertNotNull(list.get("Australia"));
		assertEquals("aus", list.get("Australia"));
	}

	@Test
	void testFindValueFromGenericList() throws RepositoryException {
		Session session = context.resourceResolver().adaptTo(Session.class);
		if (session == null) {
			throw new NullPointerException("Unable to adapt context resource resolver to session");
		}
		ValueFactory valueFactory = session.getValueFactory();
		Map<String, String> list = Maps.newHashMap();
		list.put("EDP-Noram", "edp-noram");
		list.put("ELN", "eln");
		list.put("AMN", "amn");
		Value[] value = tested.findValueFromGenericList(valueFactory, list, "ELN");
		assertNotNull(value);
		assertEquals(1, value.length);
		assertEquals("eln", value[0].getString());
	}

	@Test
	void testPopulateGenListMap_fail() {
		ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
		when(resolver.adaptTo(PageManager.class)).thenReturn(null);
		Map<String, String> list = tested.populateGenListMap(resolver,
				"/etc/acs-commons/lists/webcenter-mapping/ea-cbu");
		assertTrue(list.isEmpty());
	}

	@Test
	void testFindValueFromGenericListp_fail() {
		Value[] list = tested.findValueFromGenericList(null, new HashMap<>(), "title");
		assertEquals(1, list.length);
	}

	@Test
	void testFindRegionValue_fail() {
		ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
		String region = tested.findRegionValue(resolver,
				"/etc/acs-commons/lists/webcenter-mapping/ea-masterbrand/brand-techname/ea-masterbrand-fra-eln", "ELN");
		assertNull(region);
	}
}
