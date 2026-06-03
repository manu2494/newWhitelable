package com.merkle.xp.whitelabel.core.services.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.merkle.xp.whitelabel.core.services.ApiService;
import com.merkle.xp.whitelabel.core.services.GenericListService;
import com.merkle.xp.whitelabel.core.services.ReplicationService;

@Component(service = WorkflowProcess.class, property = { Constants.SERVICE_DESCRIPTION + "=Approve Publish CF",
		"process.label" + "=Approve Publish CF" })
public class ApprovePublishCF implements WorkflowProcess {

	@Reference
	private ReplicationService replicationService;
	
	@Reference
	private GenericListService genericList;
	
	@Reference
	ApiService apiService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovePublishCF.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
		WorkflowData wfData = workItem.getWorkflowData();
		String publishNodesString = wfData.getMetaDataMap().get("publishNodesList", String.class);
		LOGGER.warn("publishNodesString: {}", publishNodesString);
		if (publishNodesString != null) {
			publishPlatform(resourceResolver, publishNodesString);
		}
	}

	private void publishPlatform(ResourceResolver resourceResolver, String publishNodesString) {
		List<String> publishNodesList = Arrays.asList(publishNodesString.split(","));
		for (String publishNode : publishNodesList) {
			try {
				if (resourceResolver.getResource(publishNode) != null) {
					replicationService.activate(publishNode, resourceResolver);
					resourceResolver.commit();
				} else {
					LOGGER.warn("Resource " + publishNode + " does not exist for activation");
				}
			} catch (PersistenceException e) {
				LOGGER.error("Error in execute of PublishCF processing " + publishNode, e);
			}
		}
	}
}