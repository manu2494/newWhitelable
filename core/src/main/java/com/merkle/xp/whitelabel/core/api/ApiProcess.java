package com.merkle.xp.whitelabel.core.api;

import org.apache.http.NameValuePair;

import com.merkle.xp.whitelabel.core.services.impl.ApiConfiguration;

import java.util.List;
import java.util.Map;

public interface ApiProcess {
    public String getUrl();
    public HttpType getMethod();
    public List<NameValuePair> getUrlParams();
    public Map<String, String> getRequestHeaders();
    public String getBody();
	public void configure(ApiConfiguration config);
	public boolean isAdmin();
}
