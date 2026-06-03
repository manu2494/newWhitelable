package com.merkle.xp.whitelabel.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.granite.ui.components.ds.DataSource;
import com.merkle.xp.whitelabel.core.services.GenericListService;
import com.merkle.xp.whitelabel.core.testcontext.CoreComponentTestContext;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class GenericListServletTest {
    private final AemContext context = CoreComponentTestContext.newAemContext();

    @InjectMocks
    private GenericListServlet servlet = new GenericListServlet();
    @Mock
    private Resource resource;
    @Mock
    private Resource selector;
    @Mock
    private ValueMap valueMap;
    @Mock
    private GenericListService service;
    
    @BeforeEach
    void setUp() {
        context.load().json("/acscommons/lists.json", "/etc/acs-commons/lists");
        context.request().setResource(resource);
    }
    
    @Test
    void testDoGetCountries() throws ServletException, IOException {
        when(resource.getChild("datasource")).thenReturn(selector);
        when(selector.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("selector",String.class)).thenReturn("countries");
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("item1","item1title");
        data.put("item2","item2title");
        when(service.populateGenListMap(context.request().getResourceResolver(), Constants.GENERIC_LIST_BASE_PATH+"/commons/countries")).thenReturn(data);
    	servlet.doGet(context.request(), context.response());
    	DataSource ds = (DataSource) context.request().getAttribute(DataSource.class.getName());
    	assertNotNull(ds);
    	Iterator<Resource> it = ds.iterator();
    	Resource dsRes = it.next();
    	ValueMap vm = dsRes.getValueMap();
    	assertEquals("item1",vm.get("text",String.class));
    	assertEquals("item1title",vm.get("value",String.class));
    	assertTrue(it.hasNext());
    	dsRes = it.next();
    	vm = dsRes.getValueMap();
    	assertEquals("item2",vm.get("text",String.class));
    	assertEquals("item2title",vm.get("value",String.class));
    	assertFalse(it.hasNext());
    }

    @Test
    void testDoGetDummy() throws ServletException, IOException {
        when(resource.getChild("datasource")).thenReturn(selector);
        when(selector.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("selector",String.class)).thenReturn("dummy");
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("item1","item1title");
        data.put("item2","item2title");
        when(service.populateGenListMap(context.request().getResourceResolver(), Constants.GENERIC_LIST_BASE_PATH+"/dummy")).thenReturn(data);
    	servlet.doGet(context.request(), context.response());
    	DataSource ds = (DataSource) context.request().getAttribute(DataSource.class.getName());
    	assertNotNull(ds);
    	Iterator<Resource> it = ds.iterator();
    	Resource dsRes = it.next();
    	ValueMap vm = dsRes.getValueMap();
    	assertEquals("item1",vm.get("text",String.class));
    	assertEquals("item1title",vm.get("value",String.class));
    	assertTrue(it.hasNext());
    	dsRes = it.next();
    	vm = dsRes.getValueMap();
    	assertEquals("item2",vm.get("text",String.class));
    	assertEquals("item2title",vm.get("value",String.class));
    	assertFalse(it.hasNext());
    }

    @Test
    void testDoGetEmpty() throws ServletException, IOException {
        when(resource.getChild("datasource")).thenReturn(null);
        servlet.doGet(context.request(), context.response());
    	DataSource ds = (DataSource) context.request().getAttribute(DataSource.class.getName());
    	assertNotNull(ds);
    	Iterator<Resource> it = ds.iterator();
    	assertFalse(it.hasNext());
    }

    @Test
    void testDoGetFail() throws ServletException, IOException {
        when(resource.getChild("datasource")).thenReturn(selector);
        when(selector.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("selector",String.class)).thenReturn("dummy");
        doThrow(RuntimeException.class).when(service).populateGenListMap(context.request().getResourceResolver(), Constants.GENERIC_LIST_BASE_PATH+"/dummy");
    	servlet.doGet(context.request(), context.response());
    	DataSource ds = (DataSource) context.request().getAttribute(DataSource.class.getName());
    	assertNull(ds);
    }
    
    @Test
    void washupTest() {
    	MerkleLink link = new MerkleLink() {};
    	assertEquals("",link.getLabel());
    	assertEquals("",link.getTarget());
    }
}
