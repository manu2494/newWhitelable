package com.merkle.xp.whitelabel.core.utils.link;

import com.merkle.xp.whitelabel.core.utils.link.impl.LinkImpl;
import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.merkle.xp.whitelabel.core.utils.link.LinkTestUtils.*;
import static com.merkle.xp.whitelabel.core.utils.link.impl.LinkImpl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LinkImplTest {

    private static final String URL = "/url.html";

    @Test
    void testValidLink() {
        MerkleLink<Page> link = new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, null, null, null);
        assertValidLink(link, URL);
        assertNull(link.getReference());
        assertEquals(URL, link.getMappedURL());
        assertNull(link.getOriginalResourcePath());
        assertNull(link.getTarget());
    }

    @Test
    void testValidLinkWithTarget() {
        MerkleLink<Page> link = new LinkImpl(URL, URL, EXTERNALIZER_ROOT + URL, null,
                new HashMap<String, String>() {{
                    put(ATTR_TARGET, "_blank");
                }}, null);
        assertValidLink(link, URL, "_blank");
        assertNull(link.getReference());
        assertEquals("_blank",link.getTarget());
    }

    @Test
    void testValidLinkWithoutTarget() {
        MerkleLink link = new LinkImpl(URL, URL, EXTERNALIZER_ROOT + URL, null, null, null);

        assertValidLink(link, URL, (String) null);
        assertNull(link.getReference());
    }

    @Test
    void testValidLinkWithTargetAndTargetPage() {
        Page page = mock(Page.class);
        MerkleLink<Page> link = new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, page,
                new HashMap<String, String>() {{
                    put(ATTR_TARGET,
                            "_blank");
                }}, null);
        assertValidLink(link, URL, "_blank");
        assertSame(page, link.getReference());
    }

    @Test
    void testValidLinkWithTargetTargetPageAccessibilityLabelAndTitleAttribute() {
        Page page = mock(Page.class);
        MerkleLink<Page> link = new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, page, new HashMap<String, String>() {{
            put(ATTR_TARGET, "_blank");
            put(ATTR_ARIA_LABEL, "Url Label");
            put(ATTR_TITLE, "Url Title");
        }}, null);

        assertValidLink(link, URL, "Url Label", "Url Title", "_blank");
        assertSame(page, link.getReference());
    }

    @Test
    void testValidLinkWithTargetPageAccessibilityLabelTitleAttributeAndWithoutTarget() {
        Page page = mock(Page.class);
        MerkleLink<Page> link = new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, page, new HashMap<String, String>() {{
            put(ATTR_ARIA_LABEL, "Url Label");
            put(ATTR_TITLE, "Url Title");
        }}, null);

        assertValidLink(link, URL, "Url Label", "Url Title", null);
        assertSame(page, link.getReference());
    }

    @Test
    void testValidLinkWithOriginalResourcePath() {
        MerkleLink link = new LinkImpl(URL, URL, EXTERNALIZER_ROOT + URL, null, null, URL);

        assertValidLink(link, URL, (String) null);
        assertSame(URL, link.getOriginalResourcePath());
    }
    
    @Test
    void testValidLinkWithLabel() {
      MerkleLink<Page> link =
          new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, null, new HashMap<String, String>() {
            {
              put(ATTR_ARIA_LABEL, "Url Label");
              put(ATTR_TITLE, "Url Title");
            }
          }, null);

      assertSame("Url Label", link.getLabel());
    }

    @Test
    void testInvalidLink() {
        MerkleLink<Page> link = new LinkImpl<>(null, null, null, null, null, null);
        assertInvalidLink(link);
        assertNull(link.getReference());
        assertNull(link.getMappedURL());
    }

    @Test
    void testValidLikWithFilteredHtmlAttributes() {
        Page page = mock(Page.class);
        String invalidAttribute = "invalidAttribute";
        MerkleLink<Page> link = new LinkImpl<>(URL, URL, EXTERNALIZER_ROOT + URL, page, ImmutableMap.of(invalidAttribute,
                "invalidValue"), null);
        assertValidLink(link, URL);
        assertNull(link.getHtmlAttributes().get(invalidAttribute));
    }
}
