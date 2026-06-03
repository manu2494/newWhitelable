package com.merkle.xp.whitelabel.core.testcontext;

import com.adobe.cq.wcm.core.components.internal.link.DefaultPathProcessor;
import com.drew.lang.annotations.NotNull;
import io.wcm.testing.mock.aem.context.AemContextImpl;
import org.apache.sling.testing.mock.osgi.context.AbstractContextPlugin;
import org.apache.sling.testing.mock.osgi.context.ContextPlugin;

/**
 * Mock context plugins.
 */
public final class ContextPlugins {

    private ContextPlugins() {
        // constants only
    }

    /**
     * Context plugin for AEM core components.
     */
    public static final @NotNull ContextPlugin<AemContextImpl> Merkle_FRAMEWORK = new AbstractContextPlugin<AemContextImpl>() {
        @Override
        public void afterSetUp(@NotNull AemContextImpl context ) throws Exception {
            setUp(context);
        }
    };

    /**
     * Set up request context and Sling Models Extensions.
     * @param context Aem context
     */
    static void setUp(AemContextImpl context) {

        // register default path processor for core components link handling
        context.registerInjectActivateService(new DefaultPathProcessor());

    }

}
