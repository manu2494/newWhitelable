package com.merkle.xp.whitelabel.core.utils.link;

import com.adobe.cq.wcm.core.components.internal.models.v2.PageImpl;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static com.merkle.xp.whitelabel.core.utils.constants.Constants.*;
import static com.merkle.xp.whitelabel.core.utils.link.LinkTestUtils.assertValidLink;
import static com.merkle.xp.whitelabel.core.utils.link.impl.LinkManagerImpl.PN_DISABLE_SHADOWING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class LinkManagerTest {

    private final AemContext context = AppAemContext.newAemContext();

    private Page page;
    private Asset asset;

    @BeforeEach
    void setUp() {
        page = context.create().page("/content/links/site1/en/");
        asset = context.create().asset("/content/dam/asset1", 10, 10, "image/png");
        context.currentPage(page);
        context.request().setScheme("http");
        context.request().setServerName("localhost");
        context.request().setServerPort(4503);
    }

    @Test
    void testResourceEmpty() {
        Resource linkResource = context.create().resource(page, "link");
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();
        assertFalse(link.isValid());
    }

    @Test
    void testResourceExternalLink() {
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, "http://myhost");
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertEquals(link.getExternalizedURL(), "http://myhost");
        assertNull(link.getReference());
        assertEquals("http://myhost", link.getMappedURL());
    }

    @ParameterizedTest
    @ValueSource(strings = {"_blank", "_parent", "_top"})
    void testResourceExternalLinkWithAllowedTargetsAndAllAttributes(String target) {
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, "http://myhost",
                PN_LINK_TARGET, target,
                PN_LINK_ACCESSIBILITY_LABEL, "My Host Label",
                PN_LINK_TITLE_ATTRIBUTE, "My Host Title");
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertEquals(link.getExternalizedURL(), "http://myhost");
        assertEquals(ImmutableMap.of("href", "http://myhost", "aria-label", "My Host Label", "title", "My Host Title", "target", target), link.getHtmlAttributes(), "linkHtmlAttributes");
        
        assertNull(link.getReference());
    }

    @ParameterizedTest
    @ValueSource(strings = {"_self","_invalid"})
    void testResourceExternalLinkWithInvalidTargets(String target) {
        context.request().setScheme("http");
        context.request().setServerName("myhost");
        context.request().setServerPort(80);        
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, "http://myhost",
                PN_LINK_TARGET, target);
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        // invalid target or _self target should be stripped away
        assertEquals(link.getExternalizedURL(), "http://myhost");
        assertNull(link.getReference());
    }

    @Test
    void testResourcePageLink() {
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, page.getPath());
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();
        assertValidLink(link, page.getPath() + ".html");
        assertEquals(page, link.getReference());
        assertEquals((page.getPath() + ".html").replaceAll("^\\/content\\/site1\\/(.+)","/content/site1/$1"),
                link.getMappedURL());
    }

    @Test
    void testMalformedURLLink() {
        String malformedURL = "https://a:80:b/c";
        MerkleLink link = getUnderTest().get("https://a:80:b/c").build();
        assertEquals(malformedURL, link.getURL());
    }

    @Test
    void testResourceInvalidPageLink() {
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, "/content/non-existing");
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        // TODO: this link should be handled as invalid. but we keep this behavior for now to keep backwards compatibility
        assertValidLink(link, "/content/non-existing");
        assertNull(link.getReference());
    }

    @Test
    void testPageLink() {
        MerkleLink link = getUnderTest().get(page).build();

        assertValidLink(link, page.getPath() + ".html");
        assertEquals("http://localhost:4503" + page.getPath() + ".html", link.getExternalizedURL());
        assertEquals(page, link.getReference());
    }

    @Test
    void testPageLink_Null() {
        MerkleLink link = getUnderTest().get((Page)null).build();

        assertFalse(link.isValid());
    }

    @Test
    void testEmptyLink() {
        MerkleLink link = getUnderTest().get("").withLinkTarget("").build();
        assertNull(link.getURL());
        assertNull(link.getMappedURL());
        assertNull(link.getExternalizedURL());
        assertFalse(link.isValid());
    }

    @Test
    void testLinkURLPageLinkWithTarget() {
        MerkleLink link = getUnderTest().get(page.getPath()).withLinkTarget("_blank").build();

        assertValidLink(link, page.getPath() + ".html", "_blank");
        assertEquals(page, link.getReference());
    }

    @Test
    void testLinkWithTargetAsset() {
        MerkleLink link = getUnderTest().get(asset).build();

        assertValidLink(link, asset.getPath());
        assertEquals(asset, link.getReference());
    }

    @Test
    void testLinkWithUrlPropertyName() {
        MerkleLink link = getUnderTest().get(page.getPath()).withLinkUrlPropertyName("linkUrl").build();

        assertValidLink(link, page.getPath() + ".html");
        assertEquals(page, link.getReference());
    }

    /**
     * Tests a link whose target is a series of redirect pages.
     * This test confirms that link shadowing resolution functions properly.
     */
    @Test
    void testLinkWithRedirect() {
        // set up target pages
        Page targetPage1 = context.create().page(page.getPath() + "/target1");
        Page targetPage2 = context.create().page(page.getPath() + "/target2");

        // set up redirects
        Objects.requireNonNull(targetPage1.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage2.getPath());

        // create a link to the first target page
        Resource linkResource = context.create().resource(page, "link", PN_LINK_URL, targetPage1.getPath());
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertTrue(link.isValid());
        assertValidLink(link, targetPage2.getPath() + ".html");
        assertEquals("http://localhost:4503" + targetPage2.getPath() + ".html", link.getExternalizedURL());
        assertEquals(targetPage2, link.getReference());
    }

    /**
     * Tests a link whose target is a series of redirect pages - but shadowing is disabled.
     * This test confirms the ability to disable shadowing by property on the link component.
     */
    @Test
    void testLinkWithRedirect_shadowingDisabledByProperty() {
        // set up target pages
        Page targetPage1 = context.create().page(page.getPath() + "/target1");
        Page targetPage2 = context.create().page(page.getPath() + "/target2");

        // set up redirects
        Objects.requireNonNull(targetPage1.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage2.getPath());

        // create a link to the first target page
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, targetPage1.getPath(),
                PN_DISABLE_SHADOWING, Boolean.TRUE
        );
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertTrue(link.isValid());
        assertValidLink(link, targetPage1.getPath() + ".html");
        assertEquals("http://localhost:4503" + targetPage1.getPath() + ".html", link.getExternalizedURL());
        assertEquals(targetPage1, link.getReference());
    }

    /**
     * Tests a link whose target is a series of redirect pages - but shadowing is disabled.
     * This test confirms the ability to disable shadowing by the style policy.
     */
    @Test
    void testLinkWithRedirect_shadowingDisabledByStyle() {
        // set up target pages
        Page targetPage1 = context.create().page(page.getPath() + "/target1");
        Page targetPage2 = context.create().page(page.getPath() + "/target2");

        // set up redirects
        Objects.requireNonNull(targetPage1.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage2.getPath());

        // create a link to the first target page
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, targetPage1.getPath(),
                ResourceResolver.PROPERTY_RESOURCE_TYPE, "/placeholder"
        );
        context.contentPolicyMapping("/placeholder", ImmutableMap.of(
                PN_DISABLE_SHADOWING, Boolean.TRUE
        ));
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertTrue(link.isValid());
        assertValidLink(link, targetPage1.getPath() + ".html");
        assertEquals("http://localhost:4503" + targetPage1.getPath() + ".html", link.getExternalizedURL());
        assertEquals(targetPage1, link.getReference());
    }

    /**
     * Tests the ability to resolve a link when the link points to a redirect page that subsequently redirects to
     * an external site. This external link is discovered during link shadowing resolution, and is thus a different
     * test than when the link its self points to an external site.
     */
    @Test
    void testLinkWithRedirectToExternal() {
        // set up target pages
        Page targetPage1 = context.create().page(page.getPath() + "/target1");

        // set up redirects
        Objects.requireNonNull(targetPage1.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, "http://myhost");

        // create a link to the first target page
        Resource linkResource = context.create().resource(page, "link",
                PN_LINK_URL, targetPage1.getPath()
        );

        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertTrue(link.isValid());
        assertEquals(link.getExternalizedURL(), "http://myhost");
        assertEquals(targetPage1, link.getReference());
    }

    /**
     * Tests that link shadowing does not get stuck when the link target page is a redirect loop.
     */
    @Test
    void testLinkWithRedirectLoop() {
        // create three pages
        Page targetPage1 = context.create().page(page.getPath() + "/target1");
        Page targetPage2 = context.create().page(page.getPath() + "/target2");
        Page targetPage3 = context.create().page(page.getPath() + "/target3");

        // set up a redirect loop. The cycle between two and three is intentional to prevent false
        // positive test if shadowing becomes disabled.
        Objects.requireNonNull(targetPage1.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage2.getPath());
        Objects.requireNonNull(targetPage2.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage3.getPath());
        Objects.requireNonNull(targetPage3.getContentResource().adaptTo(ModifiableValueMap.class)).put(PageImpl.PN_REDIRECT_TARGET, targetPage2.getPath());

        // create a link to the first target page
        Resource linkResource = context.create().resource(page, "link", PN_LINK_URL, targetPage1.getPath());
        context.currentResource(linkResource);
        MerkleLink link = getUnderTest().get(linkResource).build();

        assertTrue(link.isValid());
        assertEquals(targetPage2, link.getReference());
    }

    private LinkManager getUnderTest() {
        return context.request().adaptTo(LinkManager.class);
    }

}
