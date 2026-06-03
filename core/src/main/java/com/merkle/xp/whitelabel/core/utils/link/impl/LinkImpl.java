package com.merkle.xp.whitelabel.core.utils.link.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;

/**
 * Wraps link information to be used in models.
 */
@JsonInclude(Include.NON_NULL)
public final class LinkImpl<T> implements MerkleLink<T> {

    public static final String ATTR_HREF = "href";
    public static final String ATTR_TARGET = "target";
    public static final String ATTR_ARIA_LABEL = "aria-label";
    public static final String ATTR_TITLE = "title";

    private static final Set<String> ALLOWED_ATTRIBUTES = new HashSet<String>() {{
        add(ATTR_TARGET);
        add(ATTR_ARIA_LABEL);
        add(ATTR_TITLE);
    }};

    private final String url;
    private final String mappedUrl;
    private final T reference;
    private final Map<String, String> htmlAttributes;
    private final String externalizedUrl;

    private final String originalResourcePath;

    public LinkImpl(@Nullable String url, @Nullable String mappedUrl, @Nullable String externalizedUrl, @Nullable T reference,
                    @Nullable Map<String, String> htmlAttributes, @Nullable String originalResourcePath) {
        this.url = url;
        this.mappedUrl = mappedUrl;
        this.externalizedUrl = externalizedUrl;
        this.reference = reference;
        this.htmlAttributes = buildHtmlAttributes(url, htmlAttributes);
        this.originalResourcePath = originalResourcePath;
    }

    /**
     * Getter exposing if link is valid.
     *
     * @return {@code true} if link is valid, {@code false} if link is not valid
     */
    @Override
    public boolean isValid() {
        return url != null;
    }

    /**
     * Getter for link URL.
     *
     * @return Link URL, can be {@code null} if link is not valid
     */
    @Override
    @JsonIgnore
    public @Nullable String getURL() {
        return url;
    }

    /**
     * Getter for the processed URL.
     *
     * @return Processed link URL, can be {@code null} if link is not valid or no processors are defined
     */
    @Override
    @JsonProperty("url")
    public @Nullable String getMappedURL() {
        return mappedUrl;
    }

    @Override
    @JsonIgnore
    public @Nullable String getExternalizedURL() {
        return externalizedUrl;
    }

    /**
     * Getter for link HTML attributes.
     *
     *
     * @return {@link Map} of HTML attributes, may include the URL as {@code href}
     */
    @Override
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("attributes")
    public @NotNull Map<String, String> getHtmlAttributes() {
        return htmlAttributes;
    }

    /**
     * Getter for link reference, if existing.
     *
     * @return Link referenced WCM/DAM entity or {@code null} if link does not point to one
     */
    @Override
    @JsonIgnore
    public @Nullable T getReference() {
        return reference;
    }
    @Override
    public @Nullable String getOriginalResourcePath() { return originalResourcePath; }
    
    @Override
    @JsonIgnore
    public @Nullable String getLabel() {
      String label = StringUtils.EMPTY;
      if(MapUtils.isNotEmpty(htmlAttributes)) {
        label = htmlAttributes.get(ATTR_ARIA_LABEL);
      }
      return label;
    }
    
    @Override
    @JsonIgnore
    public @Nullable String getTarget() {
      String target = StringUtils.EMPTY;
      if(MapUtils.isNotEmpty(htmlAttributes)) {
    	  target = htmlAttributes.get(ATTR_TARGET);
      }
      return target;
    }

    /**
     * Builds link HTML attributes.
     *
     * @param linkURL Link URL
     * @param htmlAttributes HTML attributes to add
     *
     * @return {@link Map} of link attributes
     */
    private static Map<String, String> buildHtmlAttributes(String linkURL, Map<String, String> htmlAttributes) {
        Map<String,String> attributes = new LinkedHashMap<>();
        if (linkURL != null) {
            attributes.put(ATTR_HREF, linkURL);
        }
        if (htmlAttributes != null) {
            Map<String, String> filteredAttributes = htmlAttributes.entrySet().stream()
                    .filter(e -> ALLOWED_ATTRIBUTES.contains(e.getKey()) && StringUtils.isNotEmpty(e.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            attributes.putAll(filteredAttributes);
        }
        return ImmutableMap.copyOf(attributes);
    }

}
