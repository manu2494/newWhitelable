package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import org.apache.commons.lang3.StringUtils;
import com.merkle.xp.whitelabel.core.config.SeoSiteContactPointConfig;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * The class manage data for Contact Point of Seo Site
 *
 */
@Getter
public class SeoSiteContactPoint implements ISeoSiteStructuredData {

  @SerializedName("@type")
  @Expose
  private String type = "ContactPoint";

  @Expose
  private String contactType;

  @Expose
  private String countryCode;

  @Expose
  private String telephone;

  public SeoSiteContactPoint(SeoSiteContactPointConfig cfg) {
    this.contactType = cfg.contactType();
    this.countryCode = cfg.countryCode();
    this.telephone = cfg.telephone();
  }

}
