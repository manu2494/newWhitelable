package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteLinkConfig;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoSiteSearchAction;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoSiteSearchActionTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";
  private static final String COMPONENT = PAGE + "/jcr:content/root/comp";

  @Mock
  private SeoSiteLinkConfig seoSiteLinkConfig;

  @Mock
  private LinkManager linkManager;

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
    SeoSiteSearchAction seoSiteContactPoint = new SeoSiteSearchAction(seoSiteLinkConfig);
    seoSiteContactPoint.setTarget("target");

  }

  @Test
  void test_component_with_config() {
    SeoSiteSearchAction seoSiteContactPoint = new SeoSiteSearchAction(seoSiteLinkConfig);
    seoSiteContactPoint.setTarget("target.html");
    seoSiteContactPoint.buildSeoSearchTargetUrl(linkManager);
  }
}
