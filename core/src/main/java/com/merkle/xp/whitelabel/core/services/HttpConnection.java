package com.merkle.xp.whitelabel.core.services;


import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.merkle.xp.whitelabel.core.models.ApiResponse;

public interface HttpConnection {
    public ApiResponse httpGetRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader);
    public <T extends ApiResponse> T httpGetRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, Class<T> responseType);
    public <T extends ApiResponse> T httpPutRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, String requestBody, Class<T> responseType);
	public <T extends ApiResponse> T httpPostRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, String requestBody, Class<T> responseType);
	public <T extends ApiResponse> T httpPatchRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, String requestBody, Class<T> responseType);
	public <T extends ApiResponse> T httpDeleteRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, String requestBody, Class<T> responseType);
}
