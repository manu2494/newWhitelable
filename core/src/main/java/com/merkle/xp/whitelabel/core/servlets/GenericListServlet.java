package com.merkle.xp.whitelabel.core.servlets;

import static com.merkle.xp.whitelabel.core.utils.constants.Constants.GENERIC_LIST_BASE_PATH;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.crx.JcrConstants;
import com.drew.lang.annotations.NotNull;
import com.merkle.xp.whitelabel.core.services.GenericListService;

@Component(service = Servlet.class, property = {
        "sling.servlet.resourceTypes=" + "apiServices/genericList",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.extensions=html"
})
public class GenericListServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericListServlet.class);

    @Reference
    transient GenericListService genericListService;
    @SuppressWarnings("serial")
	private static Map<String,String> listPaths = new HashMap<String, String>() {{
    	put("countries","/commons/countries");
    	put("languages","/commons/languages");
    	put("timezone","/commons/timezone");
    }};

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            ResourceResolver resolver = request.getResourceResolver();
            response.setContentType("text/html");
            LinkedHashMap<String, String> data = new LinkedHashMap<>();
            Resource resource = request.getResource();
            Resource selectorResource = resource.getChild("datasource");
            if (selectorResource != null ) {
                ValueMap valueMap = selectorResource.getValueMap();
                String selector = listPaths.get(valueMap.get("selector", String.class));
                if (selector == null) {
                	data = genericListService.populateGenListMap(resolver, GENERIC_LIST_BASE_PATH + "/"+valueMap.get("selector", String.class));
                } else {
                	data = genericListService.populateGenListMap(resolver, GENERIC_LIST_BASE_PATH + selector);
                }
            }
            LinkedHashMap<String, String> finalData = data;
            @SuppressWarnings({ "unchecked", "rawtypes" })
			DataSource ds = new SimpleDataSource(new TransformIterator<>(data.keySet().iterator(), (Transformer) obj -> {
                String dropValue = (String) obj;
                ValueMap vm = new ValueMapDecorator(new HashMap<>());
                vm.put("text", dropValue);
                vm.put("value", finalData.get(dropValue));
                return new ValueMapResource(resolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
            }));
            request.setAttribute(DataSource.class.getName(), ds);
        } catch (Exception e) {
            LOGGER.error("Exception Occurred while getting the ResourceResolver in  GenericListServlet {}",e.getMessage());
        }
    }
}
