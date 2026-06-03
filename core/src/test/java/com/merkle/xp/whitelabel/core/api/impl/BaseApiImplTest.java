package com.merkle.xp.whitelabel.core.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Test;

import com.merkle.xp.whitelabel.core.api.HttpType;
import com.merkle.xp.whitelabel.core.services.impl.ApiConfiguration;

import acscommons.com.google.common.net.HttpHeaders;

public class BaseApiImplTest {

	private BaseApiImpl api = new BaseApiImpl() {};
	
	@Test
	public void testApi() {
		api.configure(new ApiConfiguration() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return "test";
			}

			@Override
			public String serviceUrl() {
				return "https://serviceurl";
			}

			@Override
			public String username() {
				return "username";
			}

			@Override
			public String password() {
				return "password";
			}});
		assertEquals("",api.getUrl());
		assertEquals(HttpType.GET,api.getMethod());
		assertEquals(1,api.getRequestHeaders().size());
		assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=",api.getRequestHeaders().get(HttpHeaders.AUTHORIZATION));
		assertEquals("",api.getBody());
		assertFalse(api.isAdmin());
		assertEquals(HttpType.DELETE,HttpType.DELETE);
		assertEquals(HttpType.PATCH,HttpType.PATCH);
		assertEquals(HttpType.POST,HttpType.POST);
		assertEquals(HttpType.PUT,HttpType.PUT);
	}

}
