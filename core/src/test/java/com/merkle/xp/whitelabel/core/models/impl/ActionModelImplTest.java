package com.merkle.xp.whitelabel.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class })
public class ActionModelImplTest {

    private static final String COMPONENT = "/content/whitelabel-test/en/detail/jcr:content/root/comp";

    private AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    public void setup() throws Exception {
        context.load().json("/components/action-model.json", COMPONENT);

    }

    @Test
    void test_get_comp() throws Exception {
        context.currentResource(context.resourceResolver().getResource(COMPONENT));
        ActionModelImpl model = context.request().getResource().adaptTo(ActionModelImpl.class);

        assertNotNull(model);
        assertEquals("text",model.getText());
        assertEquals("/content/test/link",model.getLinkUrl());
        assertEquals("overlay",model.getOverlay());
        assertEquals("overlayState",model.getOverlayState());
    }

}
