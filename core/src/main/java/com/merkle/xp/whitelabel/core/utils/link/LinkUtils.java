package com.merkle.xp.whitelabel.core.utils.link;

import com.merkle.xp.whitelabel.core.utils.constants.Constants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static com.day.cq.wcm.api.NameConstants.PN_REDIRECT_TARGET;

public final class LinkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkUtils.class);

    private static final String CLASS_NAME = LinkUtils.class.getSimpleName();

    private LinkUtils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }


    public static String getExternalizedLink(LinkManager linkManager, String path) {
        MerkleLink link = linkManager.get(path).build();

        if (link != null) {
            return link.getExternalizedURL();
        }

        return path;
    }

    /**
     * Attempts to resolve the redirect chain starting from the given page, avoiding
     * loops.
     *
     * @param page The starting {@link Page}
     * @return A pair of {@link Page} and {@link String} the redirect chain resolves
     * to. The page can be the original page, if no redirect target is
     * defined or even {@code null} if the redirect chain does not resolve
     * to a valid page, in this case one should use the right part of the
     * pair (the {@link String} redirect target).
     */
    @NotNull
    public static Pair<Page, String> resolveRedirects(@Nullable final Page page) {
        Page result = page;
        String redirectTarget = null;
        if (page != null && page.getPageManager() != null) {
            Set<String> redirectCandidates = new LinkedHashSet<>();
            redirectCandidates.add(page.getPath());
            while (result != null && StringUtils
                    .isNotEmpty((redirectTarget = result.getProperties().get(PN_REDIRECT_TARGET, String.class)))) {
                result = page.getPageManager().getPage(redirectTarget);
                if (result != null) {
                    if (!redirectCandidates.add(result.getPath())) {
                        LOGGER.warn("[Merkle Whitelabel] - {} - Detected redirect loop for the following pages: {}.", CLASS_NAME, redirectCandidates);
                        break;
                    }
                }
            }
        }
        return new ImmutablePair<>(result, redirectTarget);
    }
}
