package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.merkle.xp.whitelabel.core.models.seo.structureddata.SeoArticleLogo;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoArticleLogoTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";
  private static final String COMPONENT = PAGE + "/jcr:content/root/comp";

  private AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  public void setup() throws Exception {
    context.load().json("/pages/home-page.json", PAGE);
    context.load().json("/components/any-node.json", COMPONENT);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);
  }

  @Test
  void test_component_with_empty_config() {
    SeoArticleLogo seoArticleLogo = new SeoArticleLogo();
    seoArticleLogo.setUrl("url");
    assertEquals(seoArticleLogo.getUrl(), "url");
    assertEquals(seoArticleLogo.getType(),"ImageObject");
  }

}
