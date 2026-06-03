package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SeoArticleTest {

  private static final String PAGE = "/content/whitelabel-test/us/en/page";

  private final AemContext context = AppAemContext.newAemContext();

  @Test
  void seoMissingInfoTest() {
    context.load().json("/pages/article-page-with-seo-missing-info.json", PAGE);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);

    context.currentResource(context.resourceResolver().getResource(PAGE + "/jcr:content/schema/article"));
    SeoArticle seoArticle = context.currentResource().adaptTo(SeoArticle.class);
    assertNotNull(seoArticle);
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
    assertEquals("{\"@context\":\"http://schema.org\",\"type\":\"Article\",\"headline\":\"en\"}",gson.toJson(seoArticle));
  }

  @Test
  void seoAllInfoTest() {
    context.load().json("/pages/article-page-with-seo.json", PAGE);

    Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
    context.currentPage(page);

    context.currentResource(context.resourceResolver().getResource(PAGE + "/jcr:content/schema/article"));
    SeoArticle seoArticle = context.currentResource().adaptTo(SeoArticle.class);
    assertNotNull(seoArticle);
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
    assertEquals("{\"@context\":\"http://schema.org\",\"type\":\"Article\",\"headline\":\"en\",\"image\":[\"/content/dam/whitelabel/logo.svg\"],\"author\":{\"type\":\"Organization\",\"name\":\"organization name\"},\"publisher\":{\"type\":\"Organization\",\"name\":\"organization name\"}}",gson.toJson(seoArticle));
  }

}
