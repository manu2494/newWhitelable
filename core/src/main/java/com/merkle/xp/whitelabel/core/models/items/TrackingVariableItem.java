package com.merkle.xp.whitelabel.core.models.items;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import lombok.Getter;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TrackingVariableItem {

  @ValueMapValue
  @Getter
  private String trackingKey;
  
  @ValueMapValue
  @Getter
  private String trackingValue;
  
  @ValueMapValue
  private String[] trackingValueTags;

  public String[] getTrackingValueTags() {
    if(ArrayUtils.isNotEmpty(trackingValueTags)) {
      return trackingValueTags.clone();
    }
    return null;
  }

}
