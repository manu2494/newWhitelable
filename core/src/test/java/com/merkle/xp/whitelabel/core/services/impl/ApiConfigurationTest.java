package com.merkle.xp.whitelabel.core.services.impl;

import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import io.wcm.testing.mock.aem.junit5.AemContext;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Session;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;

@ExtendWith({ MockitoExtension.class })
class ApiConfigurationTest {

	ApiConfigurationImpl apiConfig = new ApiConfigurationImpl();
	private static final ApiConfiguration API1 = new ApiConfiguration() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return null;
		}

		@Override
		public String name() {
			return "api1";
		}

		@Override
		public String serviceUrl() {
			return "https://api1.com/";
		}

		@Override
		public String username() {
			return "username";
		}

		@Override
		public String password() {
			return "password";
		}};
	private static final ApiConfiguration API2 = new ApiConfiguration() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return null;
		}

		@Override
		public String name() {
			return "api2";
		}

		@Override
		public String serviceUrl() {
			return "https://api2.com/";
		}

		@Override
		public String username() {
			return null;
		}

		@Override
		public String password() {
			return null;
		}};
	@BeforeEach
	void setup() {
		apiConfig.activate(API1);
		apiConfig.activate(API2);
	}

	@Test
	void testActive() throws ReplicationException {
		assertNotNull(apiConfig.getApi("api1"));
		assertNotNull(apiConfig.getApi("api2"));
		assertArrayEquals(new String[] {"api2","api1"},apiConfig.getConfigNames());
	}

	@Test
	void testDeActivate() throws ReplicationException {
		apiConfig.deactivate(API1);
		assertNull(apiConfig.getApi("api1"));
		assertNotNull(apiConfig.getApi("api2"));
		assertArrayEquals(new String[] {"api2"},apiConfig.getConfigNames());
	}

}
