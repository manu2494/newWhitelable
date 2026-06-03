package com.merkle.xp.whitelabel.core.services;

import org.apache.sling.api.resource.Resource;

import com.merkle.xp.whitelabel.core.api.ApiException;
import com.merkle.xp.whitelabel.core.api.ApiProcess;
import com.merkle.xp.whitelabel.core.models.ApiResponse;

public interface ApiService {
	public <T extends ApiResponse> T processRequest(ApiProcess apiProcess, Resource resource, Class<T> responseType) throws ApiException;
}
