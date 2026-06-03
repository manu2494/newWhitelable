package com.merkle.xp.whitelabel.core.services.impl;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

@Component(service = ParticipantStepChooser.class, property = {
        ParticipantStepChooser.SERVICE_PROPERTY_LABEL + "=Dynamic Content Approver"
})
public class DynamicContentApproverService implements ParticipantStepChooser {
    @Override
    public String getParticipant(WorkItem workItem,
                                 WorkflowSession workflowSession,
                                 MetaDataMap metaDataMap) throws WorkflowException {
        MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
        if (map.containsKey("contentApproverGroup")) {
            return map.get("contentApproverGroup").toString();
        }
        else {
            return StringUtils.EMPTY;
        }
    }
}