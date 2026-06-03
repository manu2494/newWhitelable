package com.merkle.xp.whitelabel.core.servlets.datasource;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.merkle.xp.whitelabel.core.servlets.TextValueDataResourceSource;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = Servlet.class, property = {
        "sling.servlet.resourceTypes=" + AllowedHeadingElementsDataSourceServlet.RESOURCE_TYPE_CUSTOM_HEADING,
        "sling.servlet.methods=GET",
        "sling.servlet.extensions=html"
})
public class AllowedHeadingElementsDataSourceServlet extends SlingSafeMethodsServlet {

    public static final String RESOURCE_TYPE_CUSTOM_HEADING = "whitelabel/datasources/allowedheadingelements";
    public static final String PN_ALLOWED_HEADING_ELEMENTS = "allowedHeadingElements";
    public static final String PN_DEFAULT_HEADING_ELEMENT = "headingElement";
    public static final String PN_ALLOWED_TYPES = "allowedTypes";
    public static final String PN_DEFAULT_TYPE = "type";
    public static final String PN_DEFAULT_TITLE_TYPE = "titleType";

    @Override
    public void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response)
            throws ServletException, IOException {
        SimpleDataSource allowedHeadingElementsDataSource = new SimpleDataSource(getAllowedHeadingElements(request).iterator());
        request.setAttribute(DataSource.class.getName(), allowedHeadingElementsDataSource);
    }

    private List<Resource> getAllowedHeadingElements(@NotNull SlingHttpServletRequest request) {
        List<Resource> allowedHeadingElements = new ArrayList<>();
        ResourceResolver resolver = request.getResourceResolver();
        Resource contentResource = resolver.getResource((String) request.getAttribute("granite.ui.form.contentpath"));
        ContentPolicyManager policyManager = resolver.adaptTo(ContentPolicyManager.class);
        if (contentResource != null && policyManager != null) {
            ContentPolicy policy = policyManager.getPolicy(contentResource);
            if (policy != null) {
                ValueMap props = policy.getProperties();
                if (props != null) {
                    setAllowedHeadingElementsList(allowedHeadingElements, resolver, props);
                }
            }
        }
        return allowedHeadingElements;
    }

    private void setAllowedHeadingElementsList(List<Resource> allowedHeadingElements, ResourceResolver resolver, ValueMap props) {
        String[] headingElements = props.get(PN_ALLOWED_HEADING_ELEMENTS, String[].class);
        String[] allowedTypes = props.get(PN_ALLOWED_TYPES, String[].class);
        String defaultHeadingElement = props.get(PN_DEFAULT_HEADING_ELEMENT, props.get(PN_DEFAULT_TYPE, StringUtils.EMPTY));
        if (defaultHeadingElement.isEmpty() ) {
            defaultHeadingElement = props.get(PN_DEFAULT_TITLE_TYPE, StringUtils.EMPTY);
        }
        if (headingElements == null || headingElements.length == 0) {
            headingElements = allowedTypes;
        }
        if (headingElements != null && headingElements.length > 0) {
            for (String headingElement : headingElements) {
                allowedHeadingElements.add(new HeadingElementResource(headingElement,
                        StringUtils.equals(headingElement, defaultHeadingElement), resolver));
            }
        }
    }

    private static class HeadingElementResource extends TextValueDataResourceSource {

        private final String elementName;
        private final boolean selected;

        HeadingElementResource(String headingElement, boolean defaultElement, ResourceResolver resourceResolver) {
            super(resourceResolver, StringUtils.EMPTY, RESOURCE_TYPE_NON_EXISTING);
            this.elementName = headingElement;
            this.selected = defaultElement;
        }

        @Override
        public String getText() {
            Heading heading = Heading.getHeading(elementName);
            if (heading != null) {
                return heading.getElement();
            }
            return null;
        }

        @Override
        public String getValue() {
            return elementName;
        }

        @Override
        public boolean getSelected() {
            return selected;
        }
    }

    private enum Heading {
        
        SPAN("span"),
        H1("h1"),
        H2("h2"),
        H3("h3"),
        H4("h4"),
        H5("h5"),
        H6("h6");

        private String element;

        Heading(String element) {
            this.element = element;
        }

        private static Heading getHeading(String value) {
            for (Heading heading : values()) {
                if (StringUtils.equalsIgnoreCase(heading.element, value)) {
                    return heading;
                }
            }
            return null;
        }

        public String getElement() {
            return element;
        }
    }

}
