package com.merkle.xp.whitelabel.core.services.impl;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.*;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowNode;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import java.util.List;
import java.util.ListIterator;

@Component(service = WorkflowProcess.class,
        property = {
        Constants.SERVICE_DESCRIPTION + "= Preventing content approve by Initiator",
        "process.label" + "=Prevent Approve from Initiator"
})
public class PreventApproveFromInitiatorProcess implements WorkflowProcess {
    String lastUser = StringUtils.EMPTY;
    @Override
    public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap) throws WorkflowException {
        Workflow workflow = workItem.getWorkflow();
        String initiator = workflow.getInitiator();
        List<HistoryItem> historyList = wfSession.getHistory(workflow);
        ListIterator<HistoryItem> listIterator = historyList.listIterator(historyList.size());
        while(listIterator.hasPrevious()) {
            HistoryItem lastItem = listIterator.previous();
            String stepType = lastItem.getWorkItem().getNode().getType();
            if((null != stepType) && stepType.equals(WorkflowNode.TYPE_DYNAMIC_PARTICIPANT)) {
                lastUser = lastItem.getUserId();
                break;
            }
        }
        WorkflowData wfData = workflow.getWorkflowData();
        MetaDataMap metaData = wfData.getMetaDataMap();
        if (lastUser.equals(initiator)) {
            metaData.put("isInitiator", "true");
            workItem.getMetaDataMap().put("comment","Workflow initiator does not have access to approve it.");
        }
        else {
            metaData.put("isInitiator", "false");
        }
        wfSession.updateWorkflowData(workflow,wfData);
    }
}