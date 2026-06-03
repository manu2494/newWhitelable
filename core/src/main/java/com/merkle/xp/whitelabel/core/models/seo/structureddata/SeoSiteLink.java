package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteLinkConfig;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The class for maintaining Site Link data of Seo Site
 *
 */
public class SeoSiteLink implements ISeoSiteStructuredData {

  @SerializedName("@context")
  @Expose
  @Getter
  private String context = "http://schema.org";

  @SerializedName("@type")
  @Expose
  @Getter
  private String type = "WebSite";

  @Setter
  @Expose
  @Getter
  private String url;

  @Setter
  @Expose
  @Getter
  private List<SeoSiteSearchAction> potentialAction = null;

  public SeoSiteLink (SeoSiteLinkConfig cfg) {
    this.url = cfg.url();
  }
 
}
