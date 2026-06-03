package com.merkle.xp.whitelabel.core.utils.link;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Builds a link and uses the specified link properties.
 */
@ConsumerType
public interface LinkBuilder {

    /**
     * Uses the specified property name to read the link URL from.
     *
     * @param name The property name
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkUrlPropertyName(@NotNull String name);

    /**
     * Uses the specified HTML link target.
     *
     * @param target The link target
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkTarget(@NotNull String target);

    /**
     * Uses the specified HTML link attribute.
     *
     * @param name The attribute name
     * @param value The attribute value
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkAttribute(@NotNull String name, @Nullable String value);

    /**
     * Returns the resolved link.
     *
     * @return {@link Link}
     */
    @NotNull
    MerkleLink build();
}
