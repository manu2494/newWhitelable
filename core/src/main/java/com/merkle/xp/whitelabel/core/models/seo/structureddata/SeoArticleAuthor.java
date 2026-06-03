package com.merkle.xp.whitelabel.core.models.seo.structureddata;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.google.gson.annotations.Expose;
import lombok.Getter;

/**
 * The class for maintaining Seo data related to Author Object for Article
 *
 */
@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SeoArticleAuthor {

  @ValueMapValue
  @Expose
  private String type;

  @ValueMapValue
  @Expose
  private String name;

}
