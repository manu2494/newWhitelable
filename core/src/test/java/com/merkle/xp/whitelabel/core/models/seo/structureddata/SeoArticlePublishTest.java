package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoArticlePublishTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";

  private final AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  public void setup() throws Exception {
    context.load().json("/pages/article-page-with-seo.json", PAGE);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);

    context.currentResource(context.resourceResolver().getResource(PAGE + "/jcr:content/schema/article/publisher"));
  }

  @Test
  void test_component() {
    SeoArticlePublish seoArticlePublish = context.currentResource().adaptTo(SeoArticlePublish.class);

    assertNotNull(seoArticlePublish.getLogo());
    assertNotNull(seoArticlePublish.getType());
    assertNotNull(seoArticlePublish.getName());
  }

}
