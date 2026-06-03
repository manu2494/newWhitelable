package com.merkle.xp.whitelabel.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.merkle.xp.whitelabel.core.models.MerklePageImpl;
import com.merkle.xp.whitelabel.core.services.InstanceType;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.merkle.xp.whitelabel.core.utils.ConfigurationUtils;
import com.merkle.xp.whitelabel.core.utils.ContextAwareMock;
import com.merkle.xp.whitelabel.core.utils.link.LinkBuilder;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class MerklePageImplTest {

	private static final String HOME_PAGE2 = "/content/global/promotions/merkle/site/gb/en/home";
	private static final String CONTENT_PAGE_1 = "/content/whitelabel-test/us/en/home/subpage";
	private static final String PAGE = "/content/whitelabel-test/us/en/page";
	private static final String PAGE2 = "/content/whitelabel-test/us/en/page2";
	private static final String COMPONENT = PAGE + "/jcr:content/root/comp";
	private static final String TRACKING_VARIABLES_TAG = "/content/cq:tags/core-components-examples/therapeutic_area";
	private static final String HOME_PAGE = "/content/whitelabel-test/en";
	private static final String HOME_PAGE_DE = "/content/whitelabel-test/de";
	private static final String HOME_PAGE_FR = "/content/whitelabel-test/fr";
	private static final String CONTENT_PAGE = "/content/whitelabel-test/nl/content-page";

	@Mock
	private LinkManager linkManager;
	@Mock
	private LinkBuilder linkBuilder;
	@Mock
	private MerkleLink link;
	

	@Mock
	private InstanceType instanceType;

	@Mock
	private ResourceResolver resourceResolver;

	private AemContext context = AppAemContext.newAemContext();

	static MockedStatic<ConfigurationUtils> configurationUtils;

	@BeforeAll
	static void before() {
		configurationUtils = Mockito.mockStatic(ConfigurationUtils.class);
	}

	@AfterAll
	static void after() {
		configurationUtils.close();
	}

	@BeforeEach
	public void setup() throws Exception {
		context.registerAdapter(SlingHttpServletRequest.class, LinkManager.class, linkManager);
		context.load().json("/pages/content-page-1.json", CONTENT_PAGE_1);

		Resource pageResource = context.resourceResolver().getResource(CONTENT_PAGE_1);
		context.currentResource(pageResource);

		context.load().json("/pages/home-page.json", PAGE);
		context.load().json("/pages/home-page2.json", PAGE2);
		context.load().json("/components/any-node.json", COMPONENT);

		context.load().json("/pages/home-page-with-seo-site.json", HOME_PAGE);
		context.load().json("/pages/home-page-with-seo-ignore-site-link.json", HOME_PAGE_DE);
		context.load().json("/pages/home-page-with-seo-ignore-organization.json", HOME_PAGE_FR);
		context.load().json("/pages/article-page-with-seo.json", CONTENT_PAGE);
        context.request().setScheme("http");
        context.request().setServerName("localhost");
        context.request().setServerPort(4503);
		configurationUtils.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getGeneralConfig());

		configurationUtils.when(() -> ConfigurationUtils.getTrackingConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getTrackingConfig());

		Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(PAGE);

	}
	
	private void setInstance(int type) {
		context.registerService(InstanceType.class, instanceType);
		when(instanceType.runmodes()).thenReturn(type==1?"author":"publish");
		when(instanceType.isAuthorInstance()).thenReturn(type==1?true:false);
		if (type == 1) {
			configurationUtils.when(() -> ConfigurationUtils.getDeviceConfig(any(Resource.class)))
			.thenReturn(ContextAwareMock.getDeviceConfig());
			context.load().json("/tagging/tracking_variables_tags.json", TRACKING_VARIABLES_TAG);
		} else {
			configurationUtils.when(() -> ConfigurationUtils.getDeviceConfig(any(Resource.class)))
			.thenReturn(null);
		}
	}

	@Test
	void testTracking() {
		setInstance(1);
		Resource res = context.resourceResolver().getResource(COMPONENT);

		context.currentResource(res);
		configurationUtils.when(() -> ConfigurationUtils.getTrackingConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getTrackingConfig());

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);

		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"page_category\":\"page-content\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503" + PAGE + ".html", model.getCanonicalTag());
		assertEquals(
				"{\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}],\"@context\":\"http://schema.org\",\"@type\":\"Organization\",\"name\":\"Organization name\",\"url\":\"https://www.pinterest.com.mx/site\",\"logo\":\"http://localhost:4503/content/dam/site/content/header_logo.png\",\"sameAs\":[\"https://www.facebook.com/site/\",\"https://www.instagram.com/site/\"],\"countryCode\":\"+49\",\"telephone\":\"123123\"}",
				model.getSeoSiteStructuredData());
		assertEquals("author",model.getInstance());
		assertEquals("",model.getSecure());
		assertEquals(1,model.getDevices().length);
		assertEquals(768,model.getDevices()[0].maxSize());
		assertEquals(0,model.getDevices()[0].minSize());
		assertEquals("https://www.google.com",model.getDevices()[0].redirect());

	}

	/**
	 * Testing with case ignoredOrganization is false and ignoreSiteLink is false
	 */
	@Test
	void testSeoSiteWithOrgSiteLinkSuccess() {
		setInstance(1);
		Page page = context.resourceResolver().getResource(HOME_PAGE).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE);

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);

		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"page_category\":\"page-content\",\"KEY1\":\"Override 1\",\"KEY2\":[\"Eldery Care | Oncology\"]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/en.html", model.getCanonicalTag());
		assertEquals("{\"@context\":\"http://schema.org\",\"headline\":\"en\"}", model.getSeoArticleStructuredData());
		assertEquals(
				"{\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}],\"@context\":\"http://schema.org\",\"@type\":\"Organization\",\"name\":\"Organization name\",\"url\":\"https://www.pinterest.com.mx/site\",\"logo\":\"http://localhost:4503/content/dam/site/content/header_logo.png\",\"sameAs\":[\"https://www.facebook.com/site/\",\"https://www.instagram.com/site/\"],\"countryCode\":\"+49\",\"telephone\":\"123123\"}",
				model.getSeoSiteStructuredData());
	}

	/**
	 * Testing with case ignoredOrganization is false and ignoreSiteLink is true
	 */
	@Test
	void testSeoSiteWithIgnoreSiteLinkSuccessNoLogo() {
		setInstance(1);

		Page page = context.resourceResolver().getResource(HOME_PAGE_DE).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE_DE);

		configurationUtils.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getGeneralConfig());

		configurationUtils
				.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)).seoSiteOrganization().logo())
				.thenReturn(null);

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);

		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/de.html", model.getCanonicalTag());
		assertEquals("{\"@context\":\"http://schema.org\",\"headline\":\"en\"}", model.getSeoArticleStructuredData());
	}

	@Test
	void testeoSiteWithIgnoreSiteLinkSuccess() {
		setInstance(1);

		Page page = context.resourceResolver().getResource(HOME_PAGE_DE).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE_DE);

		configurationUtils.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getGeneralConfig());

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/de.html", model.getCanonicalTag());
		assertEquals("{\"@context\":\"http://schema.org\",\"headline\":\"en\"}", model.getSeoArticleStructuredData());
		assertEquals(
				"{\"@context\":\"http://schema.org\",\"@type\":\"Organization\",\"name\":\"Organization name\",\"url\":\"https://www.pinterest.com.mx/site\",\"logo\":\"http://localhost:4503/content/dam/site/content/header_logo.png\",\"sameAs\":[\"https://www.facebook.com/site/\",\"https://www.instagram.com/site/\"],\"countryCode\":\"+49\",\"telephone\":\"123123\"}",
				model.getSeoSiteStructuredData());
	}

	/**
	 * Testing with case ignoredOrganization is true and ignoreSiteLink is false
	 */
	@Test
	void testSeoSiteWithIgnoreOrgSuccess() {
		setInstance(1);

		Page page = context.resourceResolver().getResource(HOME_PAGE_FR).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE_FR);

		configurationUtils.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)))
				.thenReturn(ContextAwareMock.getGeneralConfig());

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"page_category\":\"page-content\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/fr.html", model.getCanonicalTag());
		assertEquals("{\"@context\":\"http://schema.org\",\"headline\":\"en\"}", model.getSeoArticleStructuredData());
		assertEquals(
				"{\"@context\":\"http://schema.org\",\"@type\":\"WebSite\",\"url\":\"https://www.pinterest.com.mx/site\",\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}]}",
				model.getSeoSiteStructuredData());
	}

	/**
	 * Testing with case ignoredOrganization is true and ignoreSiteLink is false
	 */
	@Test
	void testSeoSiteWithIgnoreOrgSuccessNoUrl() {
		configurationUtils.when(() -> ConfigurationUtils.getGeneralConfig(any(Resource.class)))
		.thenReturn(ContextAwareMock.getGeneralConfig1());
		setInstance(1);

		Page page = context.resourceResolver().getResource(HOME_PAGE_FR).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE_FR);

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"page_category\":\"page-content\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/fr.html", model.getCanonicalTag());
		assertEquals("{\"@context\":\"http://schema.org\",\"headline\":\"en\"}", model.getSeoArticleStructuredData());
		assertEquals(
				"{\"@context\":\"http://schema.org\",\"@type\":\"WebSite\",\"url\":\"http://localhost:4503/content/whitelabel-test/fr.html\",\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}]}",
				model.getSeoSiteStructuredData());
	}

	/**
	 * Testing with SEO Article
	 */
	@Test
	void testSeoSiteWithArticleSuccess() {
		setInstance(1);

		Page page = context.resourceResolver().getResource(CONTENT_PAGE).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(CONTENT_PAGE);

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"author\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_name\":\"en\",\"page_category\":\"page-content\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/nl/content-page.html", model.getCanonicalTag());
		assertEquals(
				"{\"@context\":\"http://schema.org\",\"type\":\"Article\",\"headline\":\"en\",\"image\":[\"/content/dam/whitelabel/logo.svg\"],\"author\":{\"type\":\"Organization\",\"name\":\"organization name\"},\"publisher\":{\"type\":\"Organization\",\"name\":\"organization name\"}}",
				model.getSeoArticleStructuredData());
		assertEquals(
				"{\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}],\"@context\":\"http://schema.org\",\"@type\":\"Organization\",\"name\":\"Organization name\",\"url\":\"https://www.pinterest.com.mx/site\",\"logo\":\"http://localhost:4503/content/dam/site/content/header_logo.png\",\"sameAs\":[\"https://www.facebook.com/site/\",\"https://www.instagram.com/site/\"],\"countryCode\":\"+49\",\"telephone\":\"123123\"}",
				model.getSeoSiteStructuredData());
	}
	
	@Test
	void testSeoSiteWithArticleSuccessPublish() {
		setInstance(2);

		Page page = context.resourceResolver().getResource(PAGE2).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(PAGE2);

		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertNotNull(model);
		assertEquals(
				"{\"page_environment\":\"publish\",\"page_lang\":\"en\",\"page_country\":\"GB\",\"page_category\":\"page-content\",\"KEY1\":\"Value1\",\"KEY2\":[]}",
				model.getDatalayerJson());
		assertEquals("http://localhost:4503/content/whitelabel-test/us/en/page2.html", model.getCanonicalTag());
		assertEquals(
				"{\"potentialAction\":[{\"@type\":\"Search Action\",\"target\":\"http://localhost:4503/content/whitelabel-test/us/en/site/searching?q={search_term_string}\",\"query-input\":\"required name=search_term_string\"}],\"@context\":\"http://schema.org\",\"@type\":\"Organization\",\"name\":\"Organization name\",\"url\":\"https://www.pinterest.com.mx/site\",\"logo\":\"http://localhost:4503/content/dam/site/content/header_logo.png\",\"sameAs\":[\"https://www.facebook.com/site/\",\"https://www.instagram.com/site/\"],\"countryCode\":\"+49\",\"telephone\":\"123123\"}",
				model.getSeoSiteStructuredData());
		assertEquals("publish",model.getInstance());
		assertEquals("true",model.getSecure());
		assertNull(model.getDevices());
	}


	@Test
	void testCountryLanguageLocale() {
		setInstance(1);
		context.load().json("/pages/global_content_page.json", "/content/global");
		Page page = context.resourceResolver().getResource(HOME_PAGE2).adaptTo(Page.class);
		context.currentPage(page);
        context.request().setServletPath(HOME_PAGE2);
		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertEquals("UK",model.getCountryPageTitle());
		assertEquals("English",model.getLanguagePageTitle());
		assertEquals("en_GB",model.getSiteLocale());
		assertNull(model.getLoginPage());
		assertEquals("author",model.getInstance());
		assertEquals("",model.getSecure());
		assertEquals(1,model.getDevices().length);
		assertEquals(768,model.getDevices()[0].maxSize());
		assertEquals(0,model.getDevices()[0].minSize());
		assertEquals("https://www.google.com",model.getDevices()[0].redirect());
	}
	
	@Test
	void testRedirect() {
		setInstance(1);
		context.load().json("/pages/global_redirect.json", "/content/global");
		Page page = context.resourceResolver().getResource(HOME_PAGE2).adaptTo(Page.class);
		context.currentPage(page);
		context.request().setServletPath(HOME_PAGE2);
		when(linkManager.get(page)).thenReturn(linkBuilder);
		when(linkManager.get("/content/redirect")).thenReturn(linkBuilder);
		when(linkBuilder.build()).thenReturn(link);
		when(link.getMappedURL()).thenReturn("/content/redirect.html");
		when(link.getExternalizedURL()).thenReturn("/content/external.html");
		MerklePageImpl model = context.request().adaptTo(MerklePageImpl.class);
		assertEquals("/content/external.html?resource=%2Fcontent%2Fredirect.html",model.getLoginPage());
		assertNull(model.getLanguage());
		assertNull(model.getLastModifiedDate());
		assertNull(model.getKeywords());
		assertNull(model.getDesignPath());
		assertNull(model.getStaticDesignPath());
		assertNull(model.getFavicons());
		assertNull(model.getTitle());
		assertNull(model.getDescription());
		assertNull(model.getBrandSlug());
		assertNull(model.getClientLibCategories());
		assertNull(model.getClientLibCategoriesJsBody());
		assertNull(model.getClientLibCategoriesJsHead());
		assertNull(model.getTemplateName());
		assertNull(model.getAppResourcesPath());
		assertNull(model.getCssClassNames());
		assertNull(model.getRedirectTarget());
		assertFalse(model.hasCloudconfigSupport());
		assertNull(model.getComponentsResourceTypes());
		assertNull(model.getExportedItemsOrder());
		assertNull(model.getExportedItems());
		assertNull(model.getExportedType());
		assertNull(model.getMainContentSelector());
		assertNull(model.getHtmlPageItems());
		assertNull(model.getCanonicalLink());
		assertNull(model.getAlternateLanguageLinks());
		assertNull(model.getRobotsTags());
	}


}
