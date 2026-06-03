package com.merkle.xp.whitelabel.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.merkle.xp.whitelabel.core.models.ActionSocialShare;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class })
public class ActionSocialShareImplTest {

    private static final String COMPONENT = "/content/whitelabel-test/en/detail/jcr:content/root/comp";
    private static final String PAGE = "/content/whitelabel-test/en/detail";

    private final AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    public void setup() throws Exception {
        context.load().json("/pages/article-detail-page.json", PAGE);
        context.load().json("/components/action-social-share.json", COMPONENT);
        context.request().setScheme("http");
        context.request().setServerName("localhost");
        context.request().setServerPort(4503);

        Page page = context.resourceResolver().getResource(PAGE).adaptTo(Page.class);
        context.currentPage(page);
    }

    @Test
    void test_get_comp() throws Exception {
        context.currentResource(context.resourceResolver().getResource(COMPONENT));
        ActionSocialShare model = context.request().adaptTo(ActionSocialShare.class);

        assertNotNull(model);
        assertEquals("emailSubject",model.getEmailSubject());
        assertEquals("emailBody",model.getEmailBody());
        assertEquals("http://localhost:4503/content/whitelabel-test/en/detail.html",model.getSocialLink());
    }

}
