package com.merkle.xp.whitelabel.core.utils.link;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface MerkleLink<T> extends Link<T> {

   @Nullable
   default String getOriginalResourcePath() {
      return null;
   }

   default String getLabel() {
     return StringUtils.EMPTY;
   }

   default String getTarget() {
     return StringUtils.EMPTY;
   }
}
