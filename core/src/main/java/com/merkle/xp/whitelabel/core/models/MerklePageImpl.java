package com.merkle.xp.whitelabel.core.models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.models.HtmlPageItem;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.constants.NameConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.merkle.xp.whitelabel.core.config.DeviceConfig;
import com.merkle.xp.whitelabel.core.config.GeneralConfig;
import com.merkle.xp.whitelabel.core.config.SeoSiteLinkConfig;
import com.merkle.xp.whitelabel.core.config.SeoSiteOrganizationConfig;
import com.merkle.xp.whitelabel.core.config.SiteDeviceConfig;
import com.merkle.xp.whitelabel.core.config.TrackingConfig;
import com.merkle.xp.whitelabel.core.models.items.TrackingVariableItem;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.ISeoSiteStructuredData;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoArticle;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteLink;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteOrganization;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteOrganizationWithSearchAction;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteSearchAction;
import com.merkle.xp.whitelabel.core.services.InstanceType;
import com.merkle.xp.whitelabel.core.utils.ConfigurationUtils;
import com.merkle.xp.whitelabel.core.utils.PageUtils;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
import com.merkle.xp.whitelabel.core.utils.link.LinkUtils;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Page.class, resourceType = MerklePageImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MerklePageImpl implements Page {

    public static final String RESOURCE_TYPE = "whitelabel/components/page";
    private static final String SCHEMA_ARTICLE_RESOURCE_NAME_PATH = "schema/article";
    private static final String TRACKING_PATH = "jcr:content/trackingVariables";

    @Self
    @Via(type = ResourceSuperType.class)
    private Page page;

    @ScriptVariable
    private com.day.cq.wcm.api.Page currentPage;

    @Self
    private LinkManager linkManager;

    @ValueMapValue
    private String canonicalTag;

    @OSGiService
    private InstanceType instanceType;
    
    @Inject
    private Resource resource;

    @ChildResource
    @ValueMapValue
    private List<TrackingVariableItem> trackingVariables;

    @Self
    @Via("resourceResolver")
    private TagManager tagManager;

    @Inject
    private ValueMap pageProperties;

    private Gson gson;

    @Getter
    private String datalayerJson;

    @Self
    private SlingHttpServletRequest request;
    
    @Getter
    private String instance;
    
    @Getter
    private String secure;
    @Getter
    private String loginPage;
    @Getter
    private DeviceConfig[] devices;
    

    @PostConstruct
    protected void init() {
    	instance = instanceType.isAuthorInstance()?"author":"publish";
    	loginPage = getRedirectPageExternalized(getRedirectPrivatePage());
        TrackingConfig trackingConfig = ConfigurationUtils.getTrackingConfig(resource);
        secure = (Boolean.TRUE.equals(currentPage.getContentResource().getValueMap().get("isPrivatePage", Boolean.class)) && !instanceType.isAuthorInstance())?"true":"";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("page_environment", instanceType.runmodes());
        jsonObject.addProperty("page_lang", currentPage.getLanguage().getLanguage());
        jsonObject.addProperty("page_country", currentPage.getLanguage().getCountry());
        if (currentPage.getTitle() != null) {
        	try {
				jsonObject.addProperty("page_name", URLEncoder.encode(currentPage.getTitle(),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
        }
        String templatePath = currentPage.getContentResource().getValueMap().get(NameConstants.NN_TEMPLATE, String.class);
        if (templatePath != null) {
            jsonObject.addProperty("page_category", templatePath.substring(templatePath.lastIndexOf("/") + 1));
        }
        SiteDeviceConfig siteDevices = ConfigurationUtils.getDeviceConfig(resource);
        if (siteDevices != null) {
        	devices = siteDevices.devices();
        }
        ResourceResolver resolver = request.getResourceResolver();
        initTrackingVariables(resolver.getResource(trackingConfig.rootPath()), jsonObject);
        if (trackingVariables != null) {
            for (TrackingVariableItem item : trackingVariables) {
                String trackingValue = item.getTrackingValue();

                // Set tracking value from tag
                if (StringUtils.isBlank(trackingValue)) {
                    JsonArray trackingValues = getTrackingValuesFromTags(item.getTrackingValueTags());
                    jsonObject.add(item.getTrackingKey(), trackingValues);
                } else {
                    // String String tracking value
                    jsonObject.addProperty(item.getTrackingKey(), trackingValue);
                }
            }
        }
        datalayerJson = jsonObject.toString();
    }
    
	private String getRedirectPrivatePage() {
		InheritanceValueMap ivm = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
		String path = ivm.getInherited("redirectPagePrivate", String.class);
		return path;
	}
	
	private String getRedirectPageExternalized(String redirectPage) {
		if (redirectPage != null) {
			String externalizedLink = redirectPage.concat(".html");
			LinkManager linkManager = request.adaptTo(LinkManager.class);
			if (linkManager != null) {
				String originalPage = linkManager.get(currentPage).build().getMappedURL();
				String pageToRedirect = LinkUtils.getExternalizedLink(linkManager, redirectPage);
				externalizedLink = Optional.ofNullable(originalPage).map(
						pageString -> {
							try {
								return pageToRedirect + "?resource=" + URLEncoder.encode(pageString, "UTF-8");
							} catch (UnsupportedEncodingException e) {
							}
							return pageToRedirect;
						})
						.orElse(pageToRedirect);
			}
			return externalizedLink;
		}
		return redirectPage;
	}

    private JsonObject initTrackingVariables(Resource resource, JsonObject jsonObject) {
        if (resource != null) {
            Resource datalayerConfRes = resource.getChild(TRACKING_PATH);
            if (datalayerConfRes != null) {
                for (Iterator<Resource> inter = datalayerConfRes.listChildren(); inter.hasNext(); ) {
                    TrackingVariableItem item = inter.next().adaptTo(TrackingVariableItem.class);
                    buildJsonObject(jsonObject, item);
                }
            }
        }
        return jsonObject;
    }

    private void buildJsonObject(JsonObject jsonObject, TrackingVariableItem item) {
        if (item != null) { 
            String trackingValue = item.getTrackingValue();

            // Set tracking value from tag
            if (StringUtils.isBlank(trackingValue)) {
                JsonArray trackingValues = getTrackingValuesFromTags(item.getTrackingValueTags());
                jsonObject.add(item.getTrackingKey(), trackingValues);
            } else {
                // String String tracking value
                jsonObject.addProperty(item.getTrackingKey(), trackingValue);
            }
        }
    }

    public String getCanonicalTag() {
        String canonicalString = Optional.ofNullable(canonicalTag).orElse(currentPage.getPath());
        return LinkUtils.getExternalizedLink(linkManager, canonicalString);
    }

    /**
     * Build structured data related to SEO Site
     *
     * @return String Json of ISeoSiteStructuredData
     */
    public String getSeoSiteStructuredData() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
        GeneralConfig config = ConfigurationUtils.getGeneralConfig(resource);

        if (currentPage != null && config != null && pageProperties != null) {
            boolean ignoreOrganization = pageProperties.get("ignoreOrganization", Boolean.FALSE);
            boolean ignoreSitelink = pageProperties.get("ignoreSitelink", Boolean.FALSE);
            String targetSearchUrl = StringUtils.EMPTY;

            SeoSiteOrganizationConfig seoSiteOgCfg = config.seoSiteOrganization();
            SeoSiteLinkConfig seoSiteLinkCfg = config.seoSiteLinkConfig();

            if (seoSiteLinkCfg != null) {
                targetSearchUrl = seoSiteLinkCfg.target();
            }

            // Case 1: Show Organization and Site Link. Return Organization with Search Info
            if (!ignoreOrganization && !ignoreSitelink && seoSiteOgCfg != null) {
                SeoSiteOrganizationWithSearchAction result = new SeoSiteOrganizationWithSearchAction(seoSiteOgCfg);
                resolveURLOrganization(result);
                result.setPotentialAction(getSearchAction(targetSearchUrl, seoSiteLinkCfg));
                return buildStringJsonFromObject(result);
            }

            // Case 2: Ignore Site Link is true. Return Organization without Search Info
            if (!ignoreOrganization && seoSiteOgCfg != null) {
                SeoSiteOrganization result = new SeoSiteOrganization(seoSiteOgCfg);
                resolveURLOrganization(result);
                return buildStringJsonFromObject(result);
            }

            // Case 3: Ignore Organization is true. Return Seo link structured data
            if (ignoreOrganization && !ignoreSitelink && seoSiteLinkCfg != null) {
                SeoSiteLink result = new SeoSiteLink(seoSiteLinkCfg);

                // Set canonical url
                setUrl(result);

                result.setPotentialAction(getSearchAction(targetSearchUrl, seoSiteLinkCfg));
                return buildStringJsonFromObject(result);
            }
        }
        return null;
    }

    private void setUrl(SeoSiteLink result) {
        if (StringUtils.isBlank(result.getUrl())) {
            result.setUrl(getCanonicalTag());
        }
    }

    /**
     * Build structured data related to SEO Article
     *
     * @return String Json of ISeoSiteStructuredData
     */
    public String getSeoArticleStructuredData() {
        SeoArticle result = null;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();

        if (currentPage != null) {
            Resource schemaArticleResource = this.currentPage.getContentResource(SCHEMA_ARTICLE_RESOURCE_NAME_PATH);
            if (schemaArticleResource != null) {
                result = schemaArticleResource.adaptTo(SeoArticle.class);
            }
        }
        return buildStringJsonFromObject(result);
    }

    /**
     * Build String Json return for using in LD Json SEO script
     *
     * @param seoDataObj {@link ISeoSiteStructuredData}
     * @return String Json Structured data
     */
    private String buildStringJsonFromObject(ISeoSiteStructuredData seoDataObj) {
        String result = StringUtils.EMPTY;
        if (seoDataObj != null) {
            result = gson.toJson(seoDataObj);
        }
        return result;
    }

    /**
     * Resolve URL configuration with externalize for SEO Organization
     *
     * @param organization {@link SeoSiteOrganization}
     */
    private void resolveURLOrganization(SeoSiteOrganization organization) {
        if (organization != null) {
            if (StringUtils.isBlank(organization.getUrl())) {
                organization.setUrl(getCanonicalTag());
            }
            organization.setLogo(getExternalizedURL(organization.getLogo()));
        }
    }

    /**
     * Build SEO Search Action data SEO Link configuration
     *
     * @param target is Search Target page
     * @param cfg    {@link SeoSiteLinkConfig}
     */
    private List<SeoSiteSearchAction> getSearchAction(final String target, final SeoSiteLinkConfig cfg) {
        List<SeoSiteSearchAction> searchActions = null;

        if (StringUtils.isNotBlank(target)) {
            searchActions = new ArrayList<>();

            SeoSiteSearchAction searchAction = new SeoSiteSearchAction(cfg);
            searchAction.setTarget(target);
            searchAction.buildSeoSearchTargetUrl(linkManager);
            searchActions.add(searchAction);
        }

        return searchActions;
    }

    /**
     * Get Externalize for specific url
     *
     * @param url is link to get Externalize
     * @return URL link externalize
     */
    private String getExternalizedURL(String url) {
        if (StringUtils.isNotBlank(url)) {
            return LinkUtils.getExternalizedLink(linkManager, url);
        }
        return null;
    }

    /**
     * Get list tracking values from list Tag Ids
     *
     * @param tagIds is list Tag Id to get value
     * @return JsonArray
     */
    private JsonArray getTrackingValuesFromTags(String[] tagIds) {
        JsonArray trackingValues = new JsonArray();

        if (ArrayUtils.isNotEmpty(tagIds)) {
            for (String tagId : tagIds) {
                Tag tag = tagManager.resolve(tagId);

                if (tag != null) {
                    trackingValues.add(tag.getTitle());
                }
            }
        }

        return trackingValues;
    }
    
    
    
    /* Delegate methods */
    
    public String getLanguage() {
        return Optional.ofNullable(page).map(page ->page.getLanguage()).orElse(null);
    }

    public Calendar getLastModifiedDate() {
        return Optional.ofNullable(page).map(page ->page.getLastModifiedDate()).orElse(null);
    }

    @JsonIgnore
	public String[] getKeywords() {
        return Optional.ofNullable(page).map(page ->page.getKeywords()).orElse(null);
    }

    public String getDesignPath() {
        return Optional.ofNullable(page).map(page ->page.getDesignPath()).orElse(null);
    }

    public String getStaticDesignPath() {
        return Optional.ofNullable(page).map(page ->page.getStaticDesignPath()).orElse(null);
    }

    @Deprecated
    @JsonIgnore
    public Map<String, String> getFavicons() {
        return Optional.ofNullable(page).map(page ->page.getFavicons()).orElse(null);
    }

    public String getTitle() {
        return Optional.ofNullable(page).map(page ->page.getTitle()).orElse(null);
    }

    public String getDescription() {
        return Optional.ofNullable(page).map(page ->page.getDescription()).orElse(null);
    }

    public String getBrandSlug() {
        return Optional.ofNullable(page).map(page ->page.getBrandSlug()).orElse(null);
    }

    @JsonIgnore
    public String[] getClientLibCategories() {
        return Optional.ofNullable(page).map(page ->page.getClientLibCategories()).orElse(null);
    }

    @JsonIgnore
    public String[] getClientLibCategoriesJsBody() {
        return Optional.ofNullable(page).map(page ->page.getClientLibCategoriesJsBody()).orElse(null);
    }

    @JsonIgnore
    public String[] getClientLibCategoriesJsHead() {
        return Optional.ofNullable(page).map(page ->page.getClientLibCategoriesJsHead()).orElse(null);
    }

    public String getTemplateName() {
        return Optional.ofNullable(page).map(page ->page.getTemplateName()).orElse(null);
    }

    @Nullable
    public String getAppResourcesPath() {
        return Optional.ofNullable(page).map(page ->page.getAppResourcesPath()).orElse(null);
    }

    public String getCssClassNames() {
        return Optional.ofNullable(page).map(page ->page.getCssClassNames()).orElse(null);
    }

    @Nullable
    public NavigationItem getRedirectTarget() {
        return Optional.ofNullable(page).map(page ->page.getRedirectTarget()).orElse(null);
    }

    public boolean hasCloudconfigSupport() {
        return Optional.ofNullable(page).map(page ->page.hasCloudconfigSupport()).orElse(false);
    }

    @NotNull
    public Set<String> getComponentsResourceTypes() {
        return Optional.ofNullable(page).map(page ->page.getComponentsResourceTypes()).orElse(null);
    }

    @NotNull
    @Override
    public String[] getExportedItemsOrder() {
        return Optional.ofNullable(page).map(page ->page.getExportedItemsOrder()).orElse(null);
    }

    @NotNull
    @Override
    public Map<String, ? extends ComponentExporter> getExportedItems() {
        return Optional.ofNullable(page).map(page ->page.getExportedItems()).orElse(null);
    }

    @NotNull
    @Override
    public String getExportedType() {
        return Optional.ofNullable(page).map(page ->page.getExportedType()).orElse(null);
    }

    @Nullable
    public String getMainContentSelector() {
        return Optional.ofNullable(page).map(page ->page.getMainContentSelector()).orElse(null);
    }

    @Nullable
    public List<HtmlPageItem> getHtmlPageItems() {
        return Optional.ofNullable(page).map(page ->page.getHtmlPageItems()).orElse(null);
    }

    @Nullable
    @JsonIgnore
    public String getCanonicalLink() { 
        return Optional.ofNullable(page).map(page ->page.getCanonicalLink()).orElse(null);
    }

    @NotNull
    @JsonIgnore
    public Map<Locale, String> getAlternateLanguageLinks() {
        return Optional.ofNullable(page).map(page ->page.getAlternateLanguageLinks()).orElse(null);
    }

    @NotNull
    @JsonIgnore
    public List<String> getRobotsTags() {
        return Optional.ofNullable(page).map(page ->page.getRobotsTags()).orElse(null);
    }

    @NotNull
    @JsonIgnore
    public String getCountryPageTitle() {
        com.day.cq.wcm.api.Page[] localePages = PageUtils.getLocalePages(currentPage);
        if (localePages != null) {
        	return localePages[0].getTitle();
        }
        return StringUtils.EMPTY;

    }

    @NotNull
    @JsonIgnore
    public String getSiteLocale() {
        return PageUtils.getLocale(currentPage);
    }
    @NotNull
    @JsonIgnore
    public String getLanguagePageTitle() {
        com.day.cq.wcm.api.Page[] localePages = PageUtils.getLocalePages(currentPage);
        if (localePages != null) {
        	return localePages[1].getTitle();
        }
        return StringUtils.EMPTY;
    }
}
