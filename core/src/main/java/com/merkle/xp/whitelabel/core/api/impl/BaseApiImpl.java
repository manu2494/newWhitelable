package com.merkle.xp.whitelabel.core.api.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;

import com.merkle.xp.whitelabel.core.api.ApiProcess;
import com.merkle.xp.whitelabel.core.api.HttpType;
import com.merkle.xp.whitelabel.core.services.impl.ApiConfiguration;

public abstract class BaseApiImpl implements ApiProcess {

	protected ApiConfiguration config;
	
	@Override
	public String getUrl() {
		return "";
	}

	@Override
	public HttpType getMethod() {
		return HttpType.GET;
	}

	@Override
	public List<NameValuePair> getUrlParams() {
		return new ArrayList<NameValuePair>();
	}

	@Override
	public Map<String, String> getRequestHeaders() {
		Map<String, String> headers = new HashMap<>();
		if (config.username() != null) {
			addAuthentication(headers,config.username(),config.password());
		}
		return headers;
	}

	@Override
	public String getBody() {
		return "";
	}

	@Override
	public void configure(ApiConfiguration config) {
		this.config = config;
	}
	
	public boolean isAdmin() {
		return false;
	}
	
	protected void addAuthentication(Map<String, String> headers, String username, String password) {
		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
	        final String authDetails = username.concat(":").concat(password);
	        final String encodeAuthDetails = Base64.getEncoder().encodeToString(authDetails.getBytes());
			headers.put(HttpHeaders.AUTHORIZATION, "Basic " + encodeAuthDetails);
		}
	}


}
