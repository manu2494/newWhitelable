package com.merkle.xp.whitelabel.core.services.impl;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.merkle.xp.whitelabel.core.services.GenericListService;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import java.util.Map;

import static com.merkle.xp.whitelabel.core.utils.constants.Constants.*;

@Component(service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Validation for content approval payload",
                "process.label" + "=Content Approval Payload Validation Process"
        })
public class ContentApprovalPayloadValidatorProcess implements WorkflowProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentApprovalPayloadValidatorProcess.class);
    String approverGroup;
    @Reference
    GenericListService genericListService;
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        try {
            ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
            if(resolver != null ){
                Map<String,String> contentApproversMap = genericListService.populateGenListMap(resolver,GENERIC_LIST_BASE_PATH+"/content-approvers");
                Workflow wf = workItem.getWorkflow();
                WorkflowData wfData = wf.getWorkflowData();
                String payload = wfData.getPayload().toString();
                if(contentApproversMap != null) {
                    for (Map.Entry<String, String> entry : contentApproversMap.entrySet()) {
                        if (payload.startsWith(entry.getKey())) {
                            approverGroup = entry.getValue();
                            boolean isValidUser = isValidUserGroup(approverGroup, resolver);
                            if (isValidUser) {
                                wfData.getMetaDataMap().put("isValidContentPayload", "true");
                                wfData.getMetaDataMap().put("contentApproverGroup", approverGroup);
                            } else {
                                workItem.getMetaDataMap().put("comment", "Not able to find Approver-Group");
                            }
                            workflowSession.updateWorkflowData(wf, wfData);
                            break;
                        }
                    }
                }
                if(StringUtils.isEmpty(approverGroup)) {
                    workItem.getMetaDataMap().put("comment", "Path has not been configured to be used by this workflow ");
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error Occurred while payload process");
        }
    }

    private boolean isValidUserGroup(String approverGroup, ResourceResolver resolver) throws RepositoryException {
        UserManager manager = resolver.adaptTo(UserManager.class);
        if (manager != null) {
            Authorizable authorizable = manager.getAuthorizable(approverGroup);
            return (authorizable != null);
        }
        return false;
    }
}