package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteContactPointConfig;
import com.merkle.xp.whitelabel.core.config.SeoSiteOrganizationConfig;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class for maintaining Organization data of Seo Site
 *
 */
public class SeoSiteOrganization implements ISeoSiteStructuredData {

  @SerializedName("@context")
  @Expose
  private String context = "http://schema.org";

  @SerializedName("@type")
  @Expose
  private String type = "Organization";

  @Expose
  private String name;

  @Setter
  @Getter
  @Expose
  private String url;

  @Setter
  @Getter
  @Expose
  private String logo;

  @Expose
  private List<String> sameAs;

  @Expose
  private String countryCode;

  @Expose
  private String telephone;

  @Expose
  @SerializedName("contactPoint")
  private List<SeoSiteContactPoint> contactPoints;

  public SeoSiteOrganization(SeoSiteOrganizationConfig config) {
    this.name = config.name();
    this.url = config.url();
    this.logo = config.logo();
    this.countryCode = config.countryCode();
    this.telephone = config.telephone();

    if (ArrayUtils.isNotEmpty(config.sameAs())) {
      sameAs = Arrays.stream(config.sameAs()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
    
    setContactPoints(config);
  }

  /**
   * Set Contact Point from configuration
   * @param config {@link SeoSiteOrganizationConfig}
   */
  public void setContactPoints(SeoSiteOrganizationConfig config) {
    if (ArrayUtils.isNotEmpty(config.contactPoint())) {
      contactPoints = new ArrayList<>();

      for (SeoSiteContactPointConfig cfg : config.contactPoint()) {
        SeoSiteContactPoint contactPoint = new SeoSiteContactPoint(cfg);
        contactPoints.add(contactPoint);
      }

      contactPoints = contactPoints.stream()
          .filter(item -> item != null && StringUtils.isNotBlank(item.getContactType())
              && StringUtils.isNotBlank(item.getTelephone()))
          .collect(Collectors.toList());
      contactPoints = CollectionUtils.isEmpty(contactPoints) ? null : contactPoints;
    }
  }

}
