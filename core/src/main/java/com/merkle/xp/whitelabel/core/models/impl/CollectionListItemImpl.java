package com.merkle.xp.whitelabel.core.models.impl;

import static com.adobe.cq.wcm.core.components.models.List.PN_TEASER_DELEGATE;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;

import lombok.Getter;

public class CollectionListItemImpl implements ListItem{

	@Getter
    protected String linkUrl;
    @Getter
    protected String title;
    @Getter
    protected String icon;
    @Getter
    protected String description;
    @Getter
    protected Calendar lastModified;
    @Getter
    protected Resource teaserResource;

    private final String delegatedResourceType;
        
    private Page linkedPage;

    public CollectionListItemImpl(Resource pageResource, LinkManager linkManager, Component component, ResourceResolver resolver) {
    	PageManager pageManager = resolver.adaptTo(PageManager.class);
        linkedPage = Optional.ofNullable(pageManager).map(pageMan -> pageMan.getPage(pageResource.getPath())).orElse(null);
    	linkUrl = Optional.ofNullable(linkManager.get(pageResource.getPath()).build().getMappedURL()).orElse("");
    	Resource contentResource = pageResource.getChild(JcrConstants.JCR_CONTENT);
    	ValueMap contentResourceValueMap = contentResource.getValueMap();
    	delegatedResourceType = component.getProperties().get(PN_TEASER_DELEGATE, String.class);
    	if(contentResourceValueMap != null) {
    		title = retrievePropertyField(contentResourceValueMap, JcrConstants.JCR_TITLE);
        	icon = Optional.ofNullable(contentResource.getChild("cq:featuredimage"))
                    .map(Resource::getValueMap)
                    .map(valueMap -> valueMap.get("fileReference", String.class))
                    .orElse(null);
        	description = retrievePropertyField(contentResourceValueMap, JcrConstants.JCR_DESCRIPTION);
        	lastModified = contentResourceValueMap.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
        	fillTeaserResource();

    	}
    }
     
    private String retrievePropertyField(ValueMap valueMap, String property) {
    	return valueMap.get(property, String.class);
    }
    
    private void fillTeaserResource() {
        Resource resourceToBeWrapped = Optional.ofNullable(linkedPage)
                .map(Page::getContentResource)
                .map(res -> res.getChild("cq:featuredimage"))
                .orElse(null);
        if(resourceToBeWrapped != null) {
            final Map<String, Object> overriddenProperties = new HashMap<>();
            overriddenProperties.put("fileReference", icon);
            overriddenProperties.put(Teaser.PN_DESCRIPTION_FROM_PAGE, false);
            overriddenProperties.put(Teaser.PN_TITLE_FROM_PAGE, false);
            overriddenProperties.put(JcrConstants.JCR_TITLE, this.title);
            overriddenProperties.put(JcrConstants.JCR_DESCRIPTION, this.description);
            overriddenProperties.put(ImageResource.PN_LINK_URL, linkUrl);
            ValueMap overriddenValueMap = new ValueMapDecorator(overriddenProperties);
            teaserResource = new GenericResourceWrapper(resourceToBeWrapped, this.delegatedResourceType, overriddenValueMap);
        }
    }
}
