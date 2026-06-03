package com.merkle.xp.whitelabel.core.models.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.NotNull;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;

import lombok.Getter;
import lombok.experimental.Delegate;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = List.class, resourceType = "whitelabel/components/list", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomListImpl implements List {

    @Self
    @Via(type = ResourceSuperType.class)
    @Delegate(excludes = ListDelegationExclusion.class)
    protected List list;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    protected Component component;

    @ValueMapValue
    private String listFrom;

    @Inject @Via("resource")
    private String maxItems;

    @Getter
    @ValueMapValue
    private boolean wrapItemLink;

    @ChildResource
    private java.util.List<Resource> fixedList = Collections.emptyList();

    @Self
    protected LinkManager linkManager;


    @Override
    @NotNull
    public Collection<ListItem> getListItems() {
        if (StringUtils.equals(listFrom, "static")) {
            if(null != maxItems) {
                int noOfMaxItems = Integer.parseInt(maxItems);
                if (noOfMaxItems > 0) {
                    return fixedList.stream().map(this::createFixedListItem).limit(Integer.parseInt(maxItems)).collect(Collectors.toList());
                }
            }
            return fixedList.stream().map(this::createFixedListItem).collect(Collectors.toList());
        } else {
            return list.getListItems();
        }
    }

    private FixedListItemImpl createFixedListItem(Resource resource) {
        return new FixedListItemImpl.FixedListItemBuilder(resource, linkManager, component, "link").build();
    }

    private interface ListDelegationExclusion {
        Collection<ListItem> getListItems();
    }
}
