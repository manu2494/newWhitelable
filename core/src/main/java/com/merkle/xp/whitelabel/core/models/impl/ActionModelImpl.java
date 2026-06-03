package com.merkle.xp.whitelabel.core.models.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ActionModelImpl {
  
  @ValueMapValue
  @Named("link")
  @Getter
  private String linkUrl;

  @ValueMapValue
  @Getter
  private String overlay;

  @ValueMapValue
  @Getter
  private String overlayState;

  @ValueMapValue
  @Getter
  private String text;
}
