package com.merkle.xp.whitelabel.core.utils.link;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

public class LinkTest {

    @Test
    void testLinkDefaultMethods() {
        MerkleLink link = spy(MerkleLink.class);
        assertFalse(link.isValid());
        assertNull(link.getURL());
        assertNull(link.getMappedURL());
        assertNull(link.getExternalizedURL());
        assertEquals(Collections.emptyMap(), link.getHtmlAttributes());
        assertNull(link.getReference());
        assertNull(link.getOriginalResourcePath());
    }

}
