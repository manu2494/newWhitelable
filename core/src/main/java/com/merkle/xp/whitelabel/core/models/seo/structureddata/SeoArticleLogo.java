package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The class for maintaining Seo data related to Logo for Article
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SeoArticleLogo {

  @SerializedName("@type")
  @Expose
  @Getter
  private String type = "ImageObject";

  @ValueMapValue
  @Expose
  @Setter
  @Getter
  private String url;

}
