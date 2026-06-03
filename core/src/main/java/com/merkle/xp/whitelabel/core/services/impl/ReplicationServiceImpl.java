package com.merkle.xp.whitelabel.core.services.impl;

import com.merkle.xp.whitelabel.core.services.ReplicationService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

@Component(service = ReplicationService.class)
public class ReplicationServiceImpl implements ReplicationService {

	private static final Logger LOG = LoggerFactory.getLogger(ReplicationServiceImpl.class);
	@Reference
	private Replicator replicator;

	@Override
	public void activate(String path, ResourceResolver resourceResolver) {
		replicate(ReplicationActionType.ACTIVATE, path, resourceResolver);
	}

	@Override
	public void deactivate(String path, ResourceResolver resourceResolver) {
		replicate(ReplicationActionType.DEACTIVATE, path, resourceResolver);
	}

	private void replicate(ReplicationActionType replicationActionType, String path,
			ResourceResolver resourceResolver) {
		if (replicationActionType == null || StringUtils.isBlank(path) || resourceResolver == null) {
			return;
		}

		Session session = resourceResolver.adaptTo(Session.class);
		ReplicationOptions replicationOptions = new ReplicationOptions();
		replicationOptions.setSuppressVersions(true);
		try {
			this.replicator.replicate(session, replicationActionType, path, replicationOptions);
		} catch (ReplicationException e) {
			LOG.error("[Merkle Whitelabel] - ReplicationServiceImpl - Error during replication {} on path '{}' ",
					replicationActionType.getName(), path, e);

		}
	}

	@Override
	public boolean isPublished(Resource resource) {
		ReplicationStatus replicationStatus = resource.adaptTo(ReplicationStatus.class);
		return replicationStatus.isActivated();
	}

}
