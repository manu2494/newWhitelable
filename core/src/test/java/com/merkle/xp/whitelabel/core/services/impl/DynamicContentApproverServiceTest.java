package com.merkle.xp.whitelabel.core.services.impl;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DynamicContentApproverServiceTest {
    @InjectMocks
    private DynamicContentApproverService dynamicContentApproverService;
    @Mock
    WorkItem workItem;
    @Mock
    WorkflowSession workflowSession;
    @Mock
    Workflow workflow;
    @Mock
    MetaDataMap metaDataMap;
    @Mock
    WorkflowData workflowData;
    @BeforeEach
    void setUp() {
        when(workItem.getWorkflow()).thenReturn(workflow);
        when(workflow.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
    }

    @Test
    void getParticipantWithApprover() throws WorkflowException {
        when(metaDataMap.containsKey("contentApproverGroup")).thenReturn(true);
        when(metaDataMap.get("contentApproverGroup")).thenReturn("approver");
        assertEquals("approver",dynamicContentApproverService.getParticipant(workItem,workflowSession,metaDataMap));
        verifyAll();
    }
    @Test
    void getParticipantWithoutApprover() throws WorkflowException {
        when(metaDataMap.containsKey("contentApproverGroup")).thenReturn(false);
        assertEquals(StringUtils.EMPTY,dynamicContentApproverService.getParticipant(workItem,workflowSession,metaDataMap));
        verifyAll();
    }

    void verifyAll() {
        verifyNoMoreInteractions(workItem,workflowSession,workflow,workflowData);
        verifyNoMoreInteractions(metaDataMap);
    }
}