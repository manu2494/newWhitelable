package com.merkle.xp.whitelabel.core.servlets;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.SyntheticResource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import java.util.HashMap;

public abstract class TextValueDataResourceSource extends SyntheticResource {

    public static final String PN_VALUE = "value";
    public static final String PN_TEXT = "text";
    protected static final String PN_SELECTED = "selected";

    private ValueMap valueMap;

    public TextValueDataResourceSource(ResourceResolver resourceResolver, String path, String resourceType) {
        super(resourceResolver, path, resourceType);

    }

    @Override
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type == ValueMap.class) {
            if (valueMap == null) {
                initValueMap();
            }
            return (AdapterType) valueMap;
        } else {
            return super.adaptTo(type);
        }
    }

    private void initValueMap() {
        valueMap = new ValueMapDecorator(new HashMap<String, Object>());
        valueMap.put(PN_VALUE, getValue());
        valueMap.put(PN_TEXT, getText());
        valueMap.put(PN_SELECTED, getSelected());
    }

    public abstract String getText();

    public abstract String getValue();

    public abstract boolean getSelected();
}
