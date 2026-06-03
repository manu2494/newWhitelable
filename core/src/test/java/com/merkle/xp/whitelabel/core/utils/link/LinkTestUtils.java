package com.merkle.xp.whitelabel.core.utils.link;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

public final class LinkTestUtils {

    public final static String EXTERNALIZER_ROOT = "http://localhost:4503";

    public static void assertValidLink(@NotNull MerkleLink link, @NotNull String linkURL) {
        assertValidLink(link, linkURL, (SlingHttpServletRequest)null);
    }

    public static void assertValidLink(@NotNull MerkleLink link, @NotNull String linkURL,
                                       @Nullable SlingHttpServletRequest request) {
        assertTrue(link.isValid(), "linkValid");
        assertEquals(EXTERNALIZER_ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
        if (request != null && StringUtils.isNotEmpty(request.getContextPath()) && !linkURL.startsWith("http")) {
            linkURL = request.getContextPath().concat(linkURL);
        }
        assertEquals(linkURL, link.getURL(), "linkUrl");
        assertEquals(linkURL.replaceAll("^\\/content\\/site1\\/(.+)","/content/site1/$1"), link.getMappedURL(), "linkMappedUrl");
        assertEquals(ImmutableMap.of("href", linkURL), link.getHtmlAttributes(), "linkHtmlAttributes");
    }

    public static void assertValidLink(@NotNull MerkleLink link, @NotNull String linkURL, @Nullable String linkTarget) {
        if (linkTarget == null) {
            assertValidLink(link,  linkURL);
            return;
        }
        assertTrue(link.isValid(), "linkValid");
        assertEquals(linkURL, link.getURL(), "linkUrl");
        assertEquals(linkURL.replaceAll("^\\/content\\/site1\\/(.+)","/content/site1/$1"), link.getMappedURL(), "linkMappedUrl");
        assertEquals(EXTERNALIZER_ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
        assertEquals(ImmutableMap.of("href", linkURL, "target", linkTarget), link.getHtmlAttributes(), "linkHtmlAttributes");
    }

    public static void assertValidLink(@NotNull MerkleLink link, @NotNull String linkURL, @NotNull String linkAccessibilityLabel, @NotNull String linkTitleAttribute) {
        assertTrue(link.isValid(), "linkValid");
        assertEquals(linkURL, link.getURL(), "linkUrl");
        assertEquals(linkURL, link.getMappedURL(), "linkMappedUrl");
        assertEquals(EXTERNALIZER_ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
        assertEquals(ImmutableMap.of("href", linkURL, "aria-label", linkAccessibilityLabel, "title", linkTitleAttribute), link.getHtmlAttributes(), "linkHtmlAttributes");
    }

    public static void assertValidLink(@NotNull MerkleLink link, @NotNull String linkURL, @NotNull String linkAccessibilityLabel, @NotNull String linkTitleAttribute, @Nullable String linkTarget) {
        if (linkTarget == null) {
            assertValidLink(link,  linkURL, linkAccessibilityLabel, linkTitleAttribute);
            return;
        }
        assertTrue(link.isValid(), "linkValid");
        assertEquals(linkURL, link.getURL(), "linkUrl");
        assertEquals(linkURL, link.getMappedURL(), "linkMappedUrl");
        assertEquals(EXTERNALIZER_ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
        assertEquals(ImmutableMap.of("href", linkURL, "aria-label", linkAccessibilityLabel, "title", linkTitleAttribute, "target", linkTarget), link.getHtmlAttributes(), "linkHtmlAttributes");
    }

    public static void assertInvalidLink(@NotNull MerkleLink link) {
        assertFalse(link.isValid(), "linkValid");
        assertNull(link.getURL(), "linkURL");
        assertNotNull(link.getHtmlAttributes(), "linkHtmlAttributes not null");
        assertTrue(link.getHtmlAttributes().isEmpty(), "linkHtmlAttributes empty");
    }

}
