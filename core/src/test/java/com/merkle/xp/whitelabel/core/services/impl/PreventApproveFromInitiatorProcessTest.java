package com.merkle.xp.whitelabel.core.services.impl;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PreventApproveFromInitiatorProcessTest {
    @InjectMocks
    private PreventApproveFromInitiatorProcess preventApproveFromInitiatorProcess;
    @Mock
    WorkItem workItem;
    @Mock
    WorkflowSession wfSession;
    @Mock
    Workflow workflow;
    @Mock
    WorkflowData wfData;
    @Mock
    HistoryItem historyItem1;
    @Mock
    HistoryItem historyItem2;
    @Mock
    MetaDataMap metaDataMap;
    @Mock
    WorkflowNode wfNode;
    @Captor
    ArgumentCaptor<Workflow> workflowCaptor;
    @Captor
    ArgumentCaptor<WorkflowData> workflowDataCaptor;
    String initiator = "Initiator";
    String stepType = "DYNAMIC_PARTICIPANT";
    @BeforeEach
    void setUp() throws WorkflowException {
        when(workItem.getWorkflow()).thenReturn(workflow);
        when(workflow.getInitiator()).thenReturn(initiator);
        List<HistoryItem> historyList = new ArrayList<>();
        historyList.add(historyItem1);
        historyList.add(historyItem2);
        when(wfSession.getHistory(workflow)).thenReturn(historyList);
        when(historyItem2.getWorkItem()).thenReturn(workItem);
        when(workItem.getNode()).thenReturn(wfNode);
        when(wfNode.getType()).thenReturn(stepType);
        when(workflow.getWorkflowData()).thenReturn(wfData);
        when(wfData.getMetaDataMap()).thenReturn(metaDataMap);
    }
    @Test
    void ApproverExecuteTest() throws WorkflowException {
        when(historyItem2.getUserId()).thenReturn(initiator);
        when(workItem.getMetaDataMap()).thenReturn(metaDataMap);
        preventApproveFromInitiatorProcess.execute(workItem,wfSession,metaDataMap);
        verify(wfSession).updateWorkflowData(workflowCaptor.capture(),workflowDataCaptor.capture());
        assertEquals(workflow,workflowCaptor.getValue());
        assertEquals(wfData,workflowDataCaptor.getValue());
        verify(metaDataMap,times(2)).put(anyString(),anyString());
        verifyAll();
    }
    @Test
    void NonApproverExecuteTest() throws WorkflowException {
        when(historyItem2.getUserId()).thenReturn("notInitiator");
        preventApproveFromInitiatorProcess.execute(workItem,wfSession,metaDataMap);
        verify(metaDataMap,times(1)).put(anyString(),anyString());
        verify(wfSession).updateWorkflowData(workflowCaptor.capture(),workflowDataCaptor.capture());
        assertEquals(workflow,workflowCaptor.getValue());
        assertEquals(wfData,workflowDataCaptor.getValue());
        verifyAll();
    }
    void verifyAll() {
        verifyNoMoreInteractions(workItem,wfSession,workflow,wfData);
        verifyNoMoreInteractions(metaDataMap,historyItem1,historyItem2,wfNode);
    }
}