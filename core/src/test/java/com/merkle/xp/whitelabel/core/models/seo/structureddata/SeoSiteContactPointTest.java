package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteContactPointConfig;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteContactPoint;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoSiteContactPointTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";
  private static final String COMPONENT = PAGE + "/jcr:content/root/comp";

  @Mock
  private SeoSiteContactPointConfig seoSiteContactPointConfig;

  private final AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  public void setup() throws Exception {
    context.load().json("/pages/home-page.json", PAGE);
    context.load().json("/components/any-node.json", COMPONENT);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);
  }

  @Test
  void testEmptyConfig() {
    SeoSiteContactPoint seoSiteContactPoint = new SeoSiteContactPoint(seoSiteContactPointConfig);
    assertNull(seoSiteContactPoint.getContactType());
    assertEquals(seoSiteContactPoint.getType(), "ContactPoint");
    assertNull(seoSiteContactPoint.getTelephone());
    assertNull(seoSiteContactPoint.getCountryCode());

  }

  @Test
  void testFilledConfig() {
    when(seoSiteContactPointConfig.contactType()).thenReturn("technical support");
    when(seoSiteContactPointConfig.countryCode()).thenReturn("+39");
    when(seoSiteContactPointConfig.telephone()).thenReturn("123456789");
    SeoSiteContactPoint seoSiteContactPoint = new SeoSiteContactPoint(seoSiteContactPointConfig);
    assertEquals(seoSiteContactPoint.getContactType(), "technical support");
    assertEquals(seoSiteContactPoint.getType(), "ContactPoint");
    assertEquals(seoSiteContactPoint.getTelephone(), "123456789");
    assertEquals(seoSiteContactPoint.getCountryCode(), "+39");
  }

}
