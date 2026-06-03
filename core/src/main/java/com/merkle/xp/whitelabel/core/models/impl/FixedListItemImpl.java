package com.merkle.xp.whitelabel.core.models.impl;

import static com.adobe.cq.wcm.core.components.models.List.PN_TEASER_DELEGATE;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.merkle.xp.whitelabel.core.config.FileTypeIconMapConfig;
import com.merkle.xp.whitelabel.core.config.MimeTypeIconMapConfig;
import com.merkle.xp.whitelabel.core.utils.ConfigurationUtils;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;

import lombok.Getter;


public class FixedListItemImpl implements ListItem {

    @Getter
    protected MerkleLink link;
    @Getter
    protected String title;
    @Getter
    protected String icon;
    @Getter
    protected List<Tag> tags;
    @Getter
    protected String description;
    @Getter
    protected Calendar lastModified;
    @Getter
    protected String linkUrl;
    @Getter
    protected Resource teaserResource;



    private FixedListItemImpl(FixedListItemBuilder builder) {
        link = builder.link;
        linkUrl = builder.linkUrl;
        title = builder.title;
        description = builder.description;
        icon = builder.icon;
        tags = builder.tags;
        lastModified = builder.lastModified;
        teaserResource = builder.teaserResource;
    }
    

    public static class FixedListItemBuilder {
        private MerkleLink link;
        private String linkUrl;
        private String title;
        private String description;
        private String imageLink;
        private List<Tag> tags = Collections.emptyList();
        private String icon;
        private Calendar lastModified;
        private Resource teaserResource;
        private final String delegatedResourceType;

        private Resource itemResource;
        private Resource itemResourceJcrContent;
        private Page linkedPage;
        private Asset asset;
        private boolean isLinkedPage;
        private final LinkManager linkManager;


        private String getIconFromContextAware() {
            if(asset != null) {
                String assetMimeType = asset.getMimeType();
                FileTypeIconMapConfig configMap = ConfigurationUtils.getGeneralConfig(itemResource).fileTypeIconMapping();
                for(MimeTypeIconMapConfig mimeTypeMapConfig : configMap.mimeTypeIconMapConfig()) {
                    String mimeTypeConfig = mimeTypeMapConfig.mimeType();
                    if(StringUtils.equals(mimeTypeConfig, assetMimeType)) {
                        return mimeTypeMapConfig.iconClass();
                    }
                }
                if(StringUtils.contains(assetMimeType, "image")) {
                    return configMap.imageIcon();
                } else if(StringUtils.contains(assetMimeType, "video")) {
                    return configMap.videoIcon();
                } else {
                    return configMap.documentIcon();
                }
            }
            return null;
        }

        private void fillTeaserResource() {
            Resource resourceToBeWrapped = Optional.ofNullable(linkedPage)
                    .map(Page::getContentResource)
                    .map(res -> res.getChild("cq:featuredimage"))
                    .orElse(null);
            if(resourceToBeWrapped != null) {
                final Map<String, Object> overriddenProperties = new HashMap<>();
                overriddenProperties.put("fileReference", imageLink);
                overriddenProperties.put(Teaser.PN_DESCRIPTION_FROM_PAGE, false);
                overriddenProperties.put(Teaser.PN_TITLE_FROM_PAGE, false);
                overriddenProperties.put(JcrConstants.JCR_TITLE, this.title);
                overriddenProperties.put(JcrConstants.JCR_DESCRIPTION, this.description);
                overriddenProperties.put(ImageResource.PN_LINK_URL, link.getURL());
                ValueMap overriddenValueMap = new ValueMapDecorator(overriddenProperties);
                teaserResource = new GenericResourceWrapper(resourceToBeWrapped, this.delegatedResourceType, overriddenValueMap);
            }
        }

        private String retrievePropertyField(ValueMap valueMap, String property) {
            String field = valueMap.get(property, String.class);
            if(isLinkedPage && StringUtils.isBlank(field)) {
                return linkedPage.getTitle();
            }
            return field;
        }


        private void fillProperties() {
            ValueMap itemResourceValueMap = itemResource.getValueMap();
            title = retrievePropertyField(itemResourceValueMap, "title");
            description = retrievePropertyField(itemResourceValueMap, "description");
            if(itemResourceValueMap.containsKey("icon")) {
                icon = itemResourceValueMap.get("icon", String.class);
            } else {
                icon = getIconFromContextAware();
            }
            if(itemResourceJcrContent != null) {
                lastModified = itemResourceJcrContent.getValueMap().get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
            }
            if(isLinkedPage) {
                imageLink = Optional.ofNullable(linkedPage.getContentResource().getChild("cq:featuredimage"))
                        .map(Resource::getValueMap)
                        .map(valueMap -> valueMap.get("fileReference", String.class))
                        .orElse(null);
                tags = Arrays.asList(linkedPage.getTags());
            }
        }
        private void initBuilder(String linkUrlPropertyName) {
            ResourceResolver resourceResolver = itemResource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            link = linkManager.get(itemResource).withLinkUrlPropertyName(linkUrlPropertyName).build();
            linkUrl = link.getOriginalResourcePath();
            linkedPage = Optional.ofNullable(pageManager).map(pageMan -> pageMan.getPage(linkUrl)).orElse(null);
            isLinkedPage = linkedPage != null;
            Resource originalResource = resourceResolver.getResource(linkUrl);
            if(originalResource != null) {
                asset = originalResource.adaptTo(Asset.class);
                itemResourceJcrContent = originalResource.getChild(JcrConstants.JCR_CONTENT);
            }
        }
        public void setResource(Resource resource, String linkUrlPropertyName) {
            itemResource = resource;
            initBuilder(linkUrlPropertyName);
            fillProperties();
            fillTeaserResource();
        }


        public FixedListItemBuilder(Resource resource, LinkManager linkManager, Component component, String linkUrlPropertyName) {
            this.linkManager = linkManager;
            delegatedResourceType = component.getProperties().get(PN_TEASER_DELEGATE, String.class);
            setResource(resource, linkUrlPropertyName);
        }
        public FixedListItemImpl build() {
            return new FixedListItemImpl(this);
        }

    }
}
