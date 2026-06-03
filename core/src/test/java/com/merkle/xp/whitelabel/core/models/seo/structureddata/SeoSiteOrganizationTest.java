package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteContactPointConfig;
import com.merkle.xp.whitelabel.core.config.SeoSiteOrganizationConfig;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteOrganization;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoSiteOrganizationTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";
  private static final String COMPONENT = PAGE + "/jcr:content/root/comp";

  @Mock
  private SeoSiteOrganizationConfig seoSiteOrganizationConfig;

  private final AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  public void setup() throws Exception {
    context.load().json("/pages/home-page.json", PAGE);
    context.load().json("/components/any-node.json", COMPONENT);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);
  }

  @Test
  void test_component_with_empty_config() {
    SeoSiteOrganization seoSiteOrganization = new SeoSiteOrganization(seoSiteOrganizationConfig);
    seoSiteOrganization.setLogo("logo");
    assertEquals(seoSiteOrganization.getLogo(), "logo");

    seoSiteOrganization.setUrl("url");
    assertEquals(seoSiteOrganization.getUrl(), "url");

  }

  @Test
  void test_component_with_filled_config() {
    SeoSiteContactPointConfig seoSiteContactPointConfig = mock(SeoSiteContactPointConfig.class);

    when(seoSiteOrganizationConfig.contactPoint()).thenReturn(new SeoSiteContactPointConfig[]{seoSiteContactPointConfig});
    when(seoSiteOrganizationConfig.countryCode()).thenReturn("+39");
    when(seoSiteOrganizationConfig.telephone()).thenReturn("123456789");
    when(seoSiteOrganizationConfig.url()).thenReturn("url");
    when(seoSiteOrganizationConfig.name()).thenReturn("name");
    when(seoSiteOrganizationConfig.logo()).thenReturn("logo");
    when(seoSiteOrganizationConfig.sameAs()).thenReturn(new String[]{"string1", "string2"});

    SeoSiteOrganization seoSiteOrganization = new SeoSiteOrganization(seoSiteOrganizationConfig);
    seoSiteOrganization.setContactPoints(seoSiteOrganizationConfig);

  }
}
