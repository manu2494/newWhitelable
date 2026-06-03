package com.merkle.xp.whitelabel.core.models.impl;

import com.drew.lang.annotations.NotNull;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import java.util.HashMap;

public class GenericResourceWrapper extends ResourceWrapper {

    @Getter
    private ValueMap valueMap;

    private String resourceType;

    public GenericResourceWrapper(Resource resource) {
        super(resource);
        resourceType = resource.getResourceType();
        valueMap = new ValueMapDecorator(new HashMap<>(resource.getValueMap()));
    }

    public GenericResourceWrapper(Resource resource, String resourceType) {
        this(resource);
        this.resourceType = resourceType;
    }

    public GenericResourceWrapper(Resource resource, String resourceType, ValueMap valueMap) {
        this(resource, resourceType);
        this.valueMap.putAll(valueMap);
    }

    @Override
    @NotNull
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public boolean isResourceType(String resourceType) {
        return this.getResourceResolver().isResourceType(this, resourceType);
    }
}
