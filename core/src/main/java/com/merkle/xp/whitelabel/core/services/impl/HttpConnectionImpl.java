package com.merkle.xp.whitelabel.core.services.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merkle.xp.whitelabel.core.api.HttpType;
import com.merkle.xp.whitelabel.core.models.ApiResponse;
import com.merkle.xp.whitelabel.core.services.HttpConnection;

@Component(immediate = true, service = HttpConnection.class)
@ServiceDescription("Merkle CXM Http Api Connection Service")
public class HttpConnectionImpl implements HttpConnection {
	@Override
	public <T extends ApiResponse> T httpGetRequest(String url, List<NameValuePair> queryParams,
			Map<String, String> requestHeader, Class<T> responseType) {
		try {
			ApiResponse response = httpGetRequest(url, queryParams, requestHeader);
			if (response.getStatusCode() < 400) {
				T resp = new ObjectMapper().readValue(response.getResponse(), responseType);
				resp.setStatusCode(response.getStatusCode());
				return resp;
			} else {
				T resp = new ObjectMapper().readValue("{}", responseType);
				resp.setStatusCode(response.getStatusCode());
				resp.setResponse(response.getResponse());
				return resp;
			}
		} catch (IOException ex) {
			try {
				T resp = new ObjectMapper().readValue("{}", responseType);
				resp.setStatusCode(500);
				resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
						+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
						+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber() + "]");
				return resp;
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ApiResponse httpGetRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader) {

		if (StringUtils.isNotEmpty(url)) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				URIBuilder uriBuilder = new URIBuilder(url);
				if (CollectionUtils.isNotEmpty(queryParams)) {
					uriBuilder.addParameters(queryParams);
				}

				HttpGet httpGet = new HttpGet(uriBuilder.build());
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}

				try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
					return new ApiResponse(response.getStatusLine().getStatusCode(),
							EntityUtils.toString(response.getEntity()));
				}
			} catch (IOException | URISyntaxException ex) {
				try {
					ApiResponse resp = new ObjectMapper().readValue("{}", ApiResponse.class);
					resp.setStatusCode(500);
					resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
							+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
							+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber()
							+ "]");
					return resp;
				} catch (JsonProcessingException e1) {

					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public <T extends ApiResponse> T httpPutRequest(String url, List<NameValuePair> queryParams,
			Map<String, String> requestHeader, String requestBody, Class<T> responseType) {

		if (StringUtils.isNotEmpty(url)) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				URIBuilder uriBuilder = new URIBuilder(url);
				if (CollectionUtils.isNotEmpty(queryParams)) {
					uriBuilder.addParameters(queryParams);
				}

				HttpPut httpPut = new HttpPut(uriBuilder.build());
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					httpPut.setHeader(entry.getKey(), entry.getValue());
				}

				if (StringUtils.isNotEmpty(requestBody)) {
					StringEntity stringEntity = new StringEntity(requestBody);
					stringEntity.setContentType("application/json");
					httpPut.setEntity(stringEntity);
				}

				try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
					T resp = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), responseType);
					resp.setStatusCode(response.getStatusLine().getStatusCode());
					return resp;
				}
			} catch (IOException | URISyntaxException ex) {
				try {
					T resp = new ObjectMapper().readValue("{}", responseType);
					resp.setStatusCode(500);
					resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
							+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
							+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber()
							+ "]");
					return resp;
				} catch (JsonProcessingException e1) {

					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public <T extends ApiResponse> T httpDeleteRequest(String url, List<NameValuePair> queryParams, Map<String, String> requestHeader, String requestBody, Class<T> responseType) {

		if (StringUtils.isNotEmpty(url)) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				URIBuilder uriBuilder = new URIBuilder(url);
				if (CollectionUtils.isNotEmpty(queryParams)) {
					uriBuilder.addParameters(queryParams);
				}

				HttpEntityEnclosingRequestBase httpDelete = new HttpDeleteBody(uriBuilder.build());
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					httpDelete.setHeader(entry.getKey(), entry.getValue());
				}
				if (StringUtils.isNotEmpty(requestBody)) {
					StringEntity stringEntity = new StringEntity(requestBody);
					stringEntity.setContentType("application/json");
					httpDelete.setEntity(stringEntity);
				}

				try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
					T resp = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), responseType);
					resp.setStatusCode(response.getStatusLine().getStatusCode());
					return resp;
				}
			} catch (IOException | URISyntaxException ex) {
				try {
					T resp = new ObjectMapper().readValue("{}", responseType);
					resp.setStatusCode(500);
					resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
							+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
							+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber()
							+ "]");
					return resp;
				} catch (JsonProcessingException e1) {

					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public <T extends ApiResponse> T httpPostRequest(String url, List<NameValuePair> queryParams,
			Map<String, String> requestHeader, String requestBody, Class<T> responseType) {

		if (StringUtils.isNotEmpty(url)) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				URIBuilder uriBuilder = new URIBuilder(url);
				if (CollectionUtils.isNotEmpty(queryParams)) {
					uriBuilder.addParameters(queryParams);
				}

				HttpPost httpPost = new HttpPost(uriBuilder.build());
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}

				if (StringUtils.isNotEmpty(requestBody)) {
					StringEntity stringEntity = new StringEntity(requestBody);
					stringEntity.setContentType("application/json");
					httpPost.setEntity(stringEntity);
				}

				try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
					T resp = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), responseType);
					resp.setStatusCode(response.getStatusLine().getStatusCode());
					return resp;
				}
			} catch (IOException | URISyntaxException ex) {
				try {
					T resp = new ObjectMapper().readValue("{}", responseType);
					resp.setStatusCode(500);
					resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
							+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
							+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber()
							+ "]");
					return resp;
				} catch (JsonProcessingException e1) {

					e1.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public <T extends ApiResponse> T httpPatchRequest(String url, List<NameValuePair> queryParams,
			Map<String, String> requestHeader, String requestBody, Class<T> responseType) {

		if (StringUtils.isNotEmpty(url)) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				URIBuilder uriBuilder = new URIBuilder(url);
				if (CollectionUtils.isNotEmpty(queryParams)) {
					uriBuilder.addParameters(queryParams);
				}

				HttpPatch httpPatch = new HttpPatch(uriBuilder.build());
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					httpPatch.setHeader(entry.getKey(), entry.getValue());
				}

				if (StringUtils.isNotEmpty(requestBody)) {
					StringEntity stringEntity = new StringEntity(requestBody);
					stringEntity.setContentType("application/json");
					httpPatch.setEntity(stringEntity);
				}

				try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
					T resp = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), responseType);
					resp.setStatusCode(response.getStatusLine().getStatusCode());
					return resp;
				}
			} catch (IOException | URISyntaxException ex) {
				try {
					T resp = new ObjectMapper().readValue("{}", responseType);
					resp.setStatusCode(500);
					resp.setResponse("Error processing " + url + " : [" + ex.getClass().getCanonicalName() + ": "
							+ ex.getMessage() + "];[" + ex.getStackTrace()[0].getClassName() + ":"
							+ ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber()
							+ "]");
					return resp;
				} catch (JsonProcessingException e1) {

					e1.printStackTrace();
				}
			}
		}

		return null;
	}

	private class HttpDeleteBody extends HttpEntityEnclosingRequestBase {

		public HttpDeleteBody(URI uri) {
			super();
			setURI(uri);
		}

		@Override
		public String getMethod() {
			return HttpType.DELETE.name();
		}
	}
}
