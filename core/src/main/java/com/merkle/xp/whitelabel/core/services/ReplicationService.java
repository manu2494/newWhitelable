package com.merkle.xp.whitelabel.core.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Services for page activation and deactivation.
 */
public interface ReplicationService {

    /**
     * Activates the given (page or resource) {@code path}.
     *
     * @param path
     *     the path to activate
     */
    void activate(String path, ResourceResolver resourceResolver);

    /**
     * Deactivates the given (page or resource) {@code path}.
     *
     * @param path
     *     the path to deactivate
     */
    void deactivate(String path, ResourceResolver resourceResolver);
    
    /**
     * Checks if resource is published or not.
     *
     * @param resource
     *     the resource to check isPublished
     */
    boolean isPublished(Resource resource);
    
}
