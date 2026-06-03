package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteLinkConfig;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * The class for maintaining Search Action data of Seo Site
 *
 */
public class SeoSiteSearchAction implements ISeoSiteStructuredData {
  private static final String SEARCH_TERM_STRING = "{search_term_string}";
  private static final String MARKER = "?";

  @SerializedName("@type")
  @Expose
  private String type = "Search Action";

  @Setter
  @Expose
  private String target;
  
  @SerializedName("query-input")
  @Expose
  private String queryInput = "required name=search_term_string";
  
  public SeoSiteSearchAction (SeoSiteLinkConfig cfg) {
    this.target = cfg.target();
  }

  /**
   * Build Seo Search Target Url
   * 
   * @param {@link LinkManager}
   */
  public void buildSeoSearchTargetUrl(final LinkManager linkManager) {
    if (StringUtils.isNotBlank(target)) {
      String suffix = StringUtils.substringAfterLast(target, MARKER);
      
      if (target.contains(MARKER) && linkManager != null) {
        target = linkManager.get(StringUtils.substringBefore(target, MARKER)).build().getExternalizedURL();
      }

      if (StringUtils.endsWith(target, Constants.HTML_EXTENSION)) {
        target = StringUtils.substringBefore(target, Constants.HTML_EXTENSION);
      }

      this.target = target + MARKER + suffix + SEARCH_TERM_STRING;
    }
  }

}
