package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.merkle.xp.whitelabel.core.config.SeoSiteOrganizationConfig;
import com.google.gson.annotations.Expose;
import lombok.Setter;

import java.util.List;

/**
 * The class for maintaining Organization with Search Action data of Seo Site
 *
 */
public class SeoSiteOrganizationWithSearchAction extends SeoSiteOrganization {

  public SeoSiteOrganizationWithSearchAction(SeoSiteOrganizationConfig config) {
    super(config);
  }

  @Setter
  @Expose
  List<SeoSiteSearchAction> potentialAction = null;

}
