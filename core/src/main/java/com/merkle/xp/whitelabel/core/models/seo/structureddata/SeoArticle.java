package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.constants.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.merkle.xp.whitelabel.core.utils.PageUtils;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;

/**
 * The class for maintaining Site data for Seo Article
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SeoArticle implements ISeoSiteStructuredData {

    @SerializedName("@context")
    @Expose
    private String context = "http://schema.org";

    @Self
    private Resource resource;

    @ValueMapValue
    @Named("articleType")
    @Expose
    private String type;

    @ValueMapValue
    @Expose
    private String dateModified;

    @ValueMapValue
    @Expose
    private String datePublished;

    @ValueMapValue
    @Expose
    private String headline;

    @ValueMapValue
    @Expose
    private List<String> image;

    @ValueMapValue
    @Expose
    private String description;

    @ChildResource
    @Expose
    private SeoArticleAuthor author;

    @ChildResource
    @Expose
    private SeoArticlePublish publisher;

    @SlingObject
    private ResourceResolver rr;

    @Self
    private LinkManager linkManager;

    @PostConstruct
    public void init() {
        setData();
    }

    /**
     * Set fallback data if configuration is empty
     */
    private void setData() {
        Page currentPage = PageUtils.getPageFromResource(rr, resource);
        if (currentPage != null) {
          setProperties(currentPage);
        }

        if (CollectionUtils.isNotEmpty(image)) {
            image = image.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        if (author != null && StringUtils.isBlank(author.getType())
                && StringUtils.isBlank(author.getName())) {
            author = null;
        }

        if (publisher != null && StringUtils.isBlank(publisher.getType())
                && StringUtils.isBlank(publisher.getName())) {
            publisher = null;
        }

        if (publisher != null && publisher.getLogo() != null) {
            String url = publisher.getLogo().getUrl();
            if (StringUtils.isNotBlank(url) && linkManager != null) {
                publisher.getLogo().setUrl(linkManager.get(url).build().getExternalizedURL());
            }
        }
    }

  private void setProperties(Page currentPage) {
    ValueMap pageProperties = currentPage.getContentResource().getValueMap();

    if (StringUtils.isBlank(dateModified)) {
        dateModified = pageProperties.get(NameConstants.PN_PAGE_LAST_MOD, String.class);
    }

    if (StringUtils.isBlank(datePublished)) {
        datePublished = pageProperties.get(DamConstants.PN_PAGE_LAST_REPLICATED, String.class);
    }

    if (StringUtils.isBlank(headline)) {
        headline = currentPage.getTitle();
    }

    if (StringUtils.isBlank(description)) {
        description = currentPage.getDescription();
    }

    String thumbnailImg = pageProperties.get("image/fileReference", StringUtils.EMPTY);
    if (CollectionUtils.isEmpty(image) && StringUtils.isNotBlank(thumbnailImg)) {
        image = new ArrayList<>();
        image.add(thumbnailImg);
    }
  }

}
