package com.merkle.xp.whitelabel.core.utils.link;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * This interface offers a flexible way, based on the builder pattern, to compute links.
 *
 * This is a Sling model that can be injected into other models using the <code>@Self</code> annotation.
 * It can be adapted from a {@link SlingHttpServletRequest}.
 *
 * It can be used as follows:
 * <pre>
 *     Link link = linkManager.get(page).build();
 *     Link link = linkManager.get(page)
 *          .withLinkTarget(...)
 *          .withLinkAttribute(...)
 *          .build();
 * </pre>
 */
@ConsumerType
public interface LinkManager {

    /**
     * Returns a link builder where the link is defined by the resource properties.
     *
     * @param resource Resource to read the link properties from.
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder get(@NotNull Resource resource);

    /**
     * Returns a link builder where the link points to a page.
     *
     * @param page Target page of the link.
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder get(@NotNull Page page);

    /**
     * Returns a link builder where the link points to an asset.
     *
     * @param asset Target asset of the link.
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder get(@NotNull Asset asset);

    /**
     * Returns a link builder where the link points to an URL.
     *
     * @param url URL string of the link.
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder get(@NotNull String url);

}
