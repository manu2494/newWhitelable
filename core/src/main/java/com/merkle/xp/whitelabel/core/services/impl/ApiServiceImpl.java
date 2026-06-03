package com.merkle.xp.whitelabel.core.services.impl;

import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import com.merkle.xp.whitelabel.core.api.ApiException;
import com.merkle.xp.whitelabel.core.api.ApiProcess;
import com.merkle.xp.whitelabel.core.config.ApiConfig;
import com.merkle.xp.whitelabel.core.models.ApiResponse;
import com.merkle.xp.whitelabel.core.services.ApiConfigurationService;
import com.merkle.xp.whitelabel.core.services.ApiService;
import com.merkle.xp.whitelabel.core.services.HttpConnection;
import com.merkle.xp.whitelabel.core.utils.ConfigurationUtils;

@Component(immediate = true, service = ApiService.class)
@ServiceDescription("Api Service to process Http Request")
public class ApiServiceImpl implements ApiService {

		@Reference
	HttpConnection httpConnectionService;

	@Reference
	private ApiConfigurationService apiConfig;

	public <T extends ApiResponse> T processRequest(ApiProcess apiProcess, Resource resource, Class<T> responseType) throws ApiException {
		if (apiProcess.isAdmin()) {
			return processAdminRequest(apiProcess,resource, responseType);
		} else {
			return processUserRequest(apiProcess, resource, responseType);
		}
	}

	private <T extends ApiResponse> T processUserRequest(ApiProcess apiProcess, Resource resource, Class<T> responseType) throws ApiException {
		ApiConfig config = ConfigurationUtils.getApiConfig(resource);
		if (config == null) {
			throw new ApiException("config has not been defined for "+resource.getPath());
		}
		return processRequest(apiProcess, apiConfig.getApi(config.userApiName()), responseType);
	}

	private <T extends ApiResponse> T processAdminRequest(ApiProcess apiProcess, Resource resource, Class<T> responseType) throws ApiException {
		ApiConfig config = ConfigurationUtils.getApiConfig(resource);
		if (config == null) {
			throw new ApiException("config has not been defined for "+resource.getPath());
		}
		return processRequest(apiProcess, apiConfig.getApi(config.adminApiName()), responseType);
	}

	private <T extends ApiResponse> T processRequest(ApiProcess apiProcess, ApiConfiguration config, Class<T> responseType) throws ApiException {
		if (config == null) {
			return null;
		}
		apiProcess.configure(config);
		switch (apiProcess.getMethod()) {
		case GET:
			return httpConnectionService.httpGetRequest(apiProcess.getUrl(), apiProcess.getUrlParams(),
					apiProcess.getRequestHeaders(),responseType);
		case POST:
			return httpConnectionService.httpPostRequest(apiProcess.getUrl(), apiProcess.getUrlParams(),
					apiProcess.getRequestHeaders(), apiProcess.getBody(),responseType);
		case PUT:
			return httpConnectionService.httpPutRequest(apiProcess.getUrl(), apiProcess.getUrlParams(),
					apiProcess.getRequestHeaders(), apiProcess.getBody(),responseType);
		case DELETE:
			return httpConnectionService.httpDeleteRequest(apiProcess.getUrl(), apiProcess.getUrlParams(),
					apiProcess.getRequestHeaders(),apiProcess.getBody(), responseType);
		case PATCH:
			return httpConnectionService.httpPatchRequest(apiProcess.getUrl(), apiProcess.getUrlParams(),
					apiProcess.getRequestHeaders(), apiProcess.getBody(),responseType);
		default:
			throw new ApiException("Unsupported HTTP method");
		}
	}
}
