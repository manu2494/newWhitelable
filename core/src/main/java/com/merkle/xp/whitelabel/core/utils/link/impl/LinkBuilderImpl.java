package com.merkle.xp.whitelabel.core.utils.link.impl;

import static com.merkle.xp.whitelabel.core.utils.constants.Constants.ATTR_ARIA_LABEL;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.ATTR_TARGET;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.ATTR_TITLE;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.PN_LINK_ACCESSIBILITY_LABEL;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.PN_LINK_TITLE_ATTRIBUTE;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.PN_LINK_URL;
import static com.merkle.xp.whitelabel.core.utils.link.impl.LinkManagerImpl.VALID_LINK_TARGETS;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.wcm.core.components.services.link.PathProcessor;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;
import com.merkle.xp.whitelabel.core.utils.link.LinkBuilder;
import com.merkle.xp.whitelabel.core.utils.link.LinkUtils;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;

public class LinkBuilderImpl implements LinkBuilder {

    public static final String HTML_EXTENSION = ".html";

    SlingHttpServletRequest request;
    List<PathProcessor> pathProcessors;
    boolean shadowingDisabled;

    Resource linkConfiguration;
    String linkUrlPropertyName = PN_LINK_URL;
    Page targetPage;
    Asset targetAsset;
    String linkUrl;
    Map<String, String> linkAttributes = new HashMap<>();
    Object reference;
    String originalResourcePath;

    public LinkBuilderImpl(Resource resource, SlingHttpServletRequest req, List<PathProcessor> pathProcs, boolean shadowingDisabled) {
        linkConfiguration = resource;
        request = req;
        pathProcessors = pathProcs;
        this.shadowingDisabled = shadowingDisabled;
    }

    public LinkBuilderImpl(Page page, SlingHttpServletRequest req, List<PathProcessor> pathProcs, boolean shadowingDisabled) {
        targetPage = page;
        request = req;
        pathProcessors = pathProcs;
        this.shadowingDisabled = shadowingDisabled;
    }

    public LinkBuilderImpl(Asset asset, SlingHttpServletRequest req, List<PathProcessor> pathProcs) {
        targetAsset = asset;
        request = req;
        pathProcessors = pathProcs;
    }

    public LinkBuilderImpl(String url, SlingHttpServletRequest req, List<PathProcessor> pathProcs, boolean shadowingDisabled) {
        linkUrl = url;
        request = req;
        pathProcessors = pathProcs;
        this.shadowingDisabled = shadowingDisabled;
    }

    @Override
    public @NotNull LinkBuilder withLinkUrlPropertyName(@NotNull String name) {
        linkUrlPropertyName = name;
        return this;
    }

    @Override
    public @NotNull LinkBuilder withLinkTarget(@NotNull String target) {
        String resolvedLinkTarget = validateAndResolveLinkTarget(target);
        linkAttributes.put(Constants.PN_LINK_TARGET, resolvedLinkTarget);
        return this;
    }

    @Override
    public @NotNull LinkBuilder withLinkAttribute(@NotNull String name, @Nullable String value) {
        String validatedLinkAttributeValue = validateLinkAttributeValue(value);
        linkAttributes.put(name, validatedLinkAttributeValue);
        return this;
    }

    @Override
    public @NotNull MerkleLink build() {
        if (linkConfiguration != null) {
            ValueMap props = linkConfiguration.getValueMap();
            linkUrl = props.get(linkUrlPropertyName, String.class);
            originalResourcePath = linkUrl;
            String linkTarget = props.get(Constants.PN_LINK_TARGET, String.class);
            if (StringUtils.isNotEmpty(linkTarget)) {
                withLinkTarget(linkTarget);
            }
            String linkAccessibilityLabel = props.get(PN_LINK_ACCESSIBILITY_LABEL, String.class);
            if (StringUtils.isNotEmpty(linkAccessibilityLabel)) {
                withLinkAttribute(PN_LINK_ACCESSIBILITY_LABEL, linkAccessibilityLabel);
            }
            String linkTitleAttribute = props.get(PN_LINK_TITLE_ATTRIBUTE, String.class);
            if (StringUtils.isNotEmpty(linkTitleAttribute)) {
                withLinkAttribute(PN_LINK_TITLE_ATTRIBUTE, linkTitleAttribute);
            }
        } else if (targetPage != null) {
            linkUrl = targetPage.getPath();
        } else if (targetAsset != null) {
            linkUrl = targetAsset.getPath();
        }

        Asset asset = getAsset(linkUrl);
        Page page = getPage(linkUrl).orElse(null);
        if (asset != null) {
            this.reference = asset;
        } else if (page != null) {
            Pair<Page, String> pair = resolvePage(page);
            this.reference = pair.getLeft();
            linkUrl = StringUtils.isNotEmpty(pair.getRight()) ? pair.getRight() : linkUrl;
        }
        String resolvedLinkURL = validateAndResolveLinkURL(linkUrl);
        Map<String, String> htmlAttributes = mapLinkAttributesToHtml();
        return buildLink(resolvedLinkURL, request, htmlAttributes);
    }

    private @NotNull MerkleLink buildLink(String path, SlingHttpServletRequest request, Map<String, String> htmlAttributes) {
        if (StringUtils.isNotEmpty(path)) {
            try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
            String decodedPath = path;
            return pathProcessors.stream()
                    .filter(pathProcessor -> pathProcessor.accepts(decodedPath, request))
                    .findFirst()
                    .map(pathProcessor -> new LinkImpl(
                            pathProcessor.sanitize(decodedPath, request),
                            pathProcessor.map(decodedPath, request),
                            convertExternalPath(pathProcessor.externalize(decodedPath, request), request,decodedPath),
                            this.reference,
                            pathProcessor.processHtmlAttributes(decodedPath, htmlAttributes),
                            originalResourcePath))
                    .orElse(new LinkImpl(path, path, path, this.reference, htmlAttributes, originalResourcePath));
        } else {
            return new LinkImpl(path, path, path, this.reference, htmlAttributes, originalResourcePath);
        }
    }

    private String convertExternalPath(String externalPath,
			SlingHttpServletRequest request, String decodedPath) {
    	if (decodedPath.startsWith("http")) {
    		return decodedPath;
    	}
    	if (externalPath.startsWith("http")) {
			try {
				URL url = new URL(externalPath);
	    		URL requestUrl = new URL(request.getRequestURL().toString());
	    		String protocol = "https";
	    		if (requestUrl.getHost().equals("localhost")) {
	    			protocol="http";
	    		}
	    		URL newUrl = new URL(protocol,requestUrl.getHost(),requestUrl.getPort(),url.getFile());
	    		return newUrl.toExternalForm();
			} catch (MalformedURLException e) {
			}
    	}
		return decodedPath;
	}

	@NotNull
    private Map<String, String> mapLinkAttributesToHtml() {
        return new HashMap<String, String>() {{
            put(ATTR_TARGET, linkAttributes.get(Constants.PN_LINK_TARGET));
            put(ATTR_ARIA_LABEL, linkAttributes.get(PN_LINK_ACCESSIBILITY_LABEL));
            put(ATTR_TITLE, linkAttributes.get(PN_LINK_TITLE_ATTRIBUTE));
        }};
    }

    /**
     * Validates and resolves the link target.
     * @param linkTarget Link target
     *
     * @return The validated link target or {@code null} if not valid
     */
    @Nullable
    private String validateAndResolveLinkTarget(@Nullable final String linkTarget) {
        return Optional.ofNullable(linkTarget)
                .filter(VALID_LINK_TARGETS::contains)
                .orElse(null);
    }

    /**
     * Validates the link accessibility label.
     * @param value Link accessibility label
     *
     * @return The validated link accessibility label or {@code null} if not valid
     */
    @Nullable
    private String validateLinkAttributeValue(@Nullable final String value) {
        return Optional.ofNullable(value)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .orElse(null);
    }

    /**
     * Validates and resolves a link URL.
     *
     * @param linkURL Link URL
     * @return The validated link URL or {@code null} if not valid
     */
    @Nullable
    private String validateAndResolveLinkURL(@Nullable final String linkURL) {
        return Optional.ofNullable(linkURL)
                .filter(StringUtils::isNotEmpty)
                .map(this::getLinkURL)
                .orElse(null);
    }

    /**
     * If the provided {@code path} identifies a {@link Page}, this method will generate the correct URL for the page. Otherwise the
     * original {@code String} is returned.
     * @param path the page path
     *
     * @return the URL of the page identified by the provided {@code path}, or the original {@code path} if this doesn't identify a {@link Page}
     */
    @NotNull
    private String getLinkURL(@NotNull String path) {
        return getPage(path)
                .map(this::getPageLinkURL)
                .orElse(path);
    }

    /**
     * Given a {@link Page}, this method returns the correct URL with the extension
     * @param page the page
     *
     * @return the URL of the provided (@code page}
     */
    @NotNull
    private String getPageLinkURL(@NotNull Page page) {
        return page.getPath() + HTML_EXTENSION;
    }

    /**
     * Returns the DAM asset referenced by the path.
     *
     * @param path The reference path
     * @return the DAM asset referenced by the path.
     */
    @Nullable
    private Asset getAsset(@NotNull String path) {
        return Optional.ofNullable(request.getResourceResolver().getResource(path))
                .map(assetRes -> assetRes.adaptTo(Asset.class))
                .orElse(null);
    }

    /**
     * Given a path, tries to resolve to the corresponding page.
     *
     * @param path The path
     * @return The {@link Page} corresponding to the path
     */
    @NotNull
    private Optional<Page> getPage(@Nullable String path) {
        return Optional.ofNullable(path)
                .flatMap(p -> Optional.ofNullable(this.request.getResourceResolver().adaptTo(PageManager.class))
                        .map(pm -> pm.getPage(p)));
    }

    /**
     * Attempts to resolve a Link URL and page for the given page. Redirect chains are followed, if
     * shadowing is not disabled.
     *
     * @param page Page
     * @return A pair of {@link String} and {@link Page} the page resolves to.
     */
    @NotNull
    private Pair<Page, String> resolvePage(@NotNull final Page page) {
        Pair<Page, String> pair = !shadowingDisabled ? LinkUtils.resolveRedirects(page) : new ImmutablePair<>(page, null);
        if (pair.getLeft() == null && StringUtils.isNotEmpty(pair.getRight())) {
            return new ImmutablePair<>(page, pair.getRight());
        }
        Page resolved = Optional.ofNullable(pair.getLeft()).orElse(page);
        return new ImmutablePair<>(resolved, getPageLinkURL(resolved));
    }
}
