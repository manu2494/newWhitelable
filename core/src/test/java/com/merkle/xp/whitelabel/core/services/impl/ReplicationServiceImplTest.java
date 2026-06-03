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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith({ MockitoExtension.class })
class ReplicationServiceImplTest {

	AemContext context = new AemContext(ResourceResolverType.RESOURCERESOLVER_MOCK);
	@InjectMocks
	ReplicationServiceImpl replicationService = new ReplicationServiceImpl();

	@Mock
	private ResourceResolver resourceResolver;
	@Mock
	Replicator replicator;
	@Mock
	Session session;
	@Mock
	Resource resource;
	@Mock
	ReplicationStatus replicationStatus;

	@BeforeEach
	void setup() {

	}

	@Test
	void testActivate() throws ReplicationException {
		replicationService.activate(null, resourceResolver);
		verify(replicator, times(0)).replicate(any(), any(), anyString(), any());

		when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		replicationService.deactivate("path", resourceResolver);
		verify(replicator, times(1)).replicate(any(), any(), anyString(), any());

		ReplicationOptions replicationOptions = new ReplicationOptions();
		replicationOptions.setSuppressVersions(true);
		doThrow(new ReplicationException("Exception")).when(replicator).replicate(any(), any(), anyString(), any());
		replicationService.deactivate("path", resourceResolver);
	}

	@Test
	void testIsPublished() {
		when(replicationStatus.isActivated()).thenReturn(true);
		when(resource.adaptTo(ReplicationStatus.class)).thenReturn(replicationStatus);
		assertTrue(replicationService.isPublished(resource));
		when(replicationStatus.isActivated()).thenReturn(false);
		assertFalse(replicationService.isPublished(resource));
	}
}
