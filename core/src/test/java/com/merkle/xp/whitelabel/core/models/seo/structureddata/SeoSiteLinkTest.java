package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteLinkConfig;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteLink;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoSiteLinkTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";
  private static final String COMPONENT = PAGE + "/jcr:content/root/comp";

  @Mock
  private SeoSiteLinkConfig seoSiteLinkConfig;

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
    SeoSiteLink seoSiteLink = new SeoSiteLink(seoSiteLinkConfig);
    seoSiteLink.setUrl("url");
    seoSiteLink.setPotentialAction(Collections.emptyList());

    assertEquals(seoSiteLink.getUrl(),"url");
    assertEquals(seoSiteLink.getContext(),"http://schema.org");
    assertEquals(seoSiteLink.getType(),"WebSite");
    assertNotNull(seoSiteLink.getPotentialAction());
    assertEquals(seoSiteLink.getPotentialAction().size(),0);
    

  }

}
