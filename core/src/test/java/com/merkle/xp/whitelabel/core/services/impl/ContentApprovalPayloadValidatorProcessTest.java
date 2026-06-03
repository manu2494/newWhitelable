package com.merkle.xp.whitelabel.core.services.impl;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.merkle.xp.whitelabel.core.services.GenericListService;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.jcr.RepositoryException;
import java.util.LinkedHashMap;
import static com.merkle.xp.whitelabel.core.utils.constants.Constants.GENERIC_LIST_BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentApprovalPayloadValidatorProcessTest {

    @InjectMocks
    private ContentApprovalPayloadValidatorProcess contentApprovalPayloadValidatorProcess;
    @Mock
    WorkItem workItem;
    @Mock
    WorkflowSession workflowSession;
    @Mock
    Workflow workflow;
    @Mock
    MetaDataMap metaDataMap;
    @Mock
    ResourceResolver resolver;
    @Mock
    WorkflowData workflowData;
    @Mock
    GenericListService genericListService;
    LinkedHashMap<String,String> map;
    @Mock
    UserManager userManager;
    @Mock
    Authorizable authorizable;
    @Captor
    ArgumentCaptor<Workflow> workflowCaptor;
    @Captor
    ArgumentCaptor<WorkflowData> workflowDataCaptor;
    @Captor
    ArgumentCaptor<String> approverCaptor;
    @BeforeEach
    void setUp() {
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        map = new LinkedHashMap<>();
        map.put("path2","approver2");
        when(workItem.getWorkflow()).thenReturn(workflow);
        when(workflow.getWorkflowData()).thenReturn(workflowData);
        String payload = "/content/global/merkle";
        when(workflowData.getPayload()).thenReturn(payload);
        when(genericListService.populateGenListMap(resolver,GENERIC_LIST_BASE_PATH+"/content-approvers")).thenReturn(map);
    }
    @Test
    void executeWithPayloadAndApprover() throws RepositoryException, WorkflowException {
        map.put("/content/global/merkle","approver1");
        when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
        when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
        when(userManager.getAuthorizable("approver1")).thenReturn(authorizable);
        contentApprovalPayloadValidatorProcess.execute(workItem,workflowSession,metaDataMap);
        verify(userManager).getAuthorizable(approverCaptor.capture());
        assertEquals("approver1",approverCaptor.getValue());
        verify(workflowSession).updateWorkflowData(workflowCaptor.capture(),workflowDataCaptor.capture());
        assertEquals(workflow,workflowCaptor.getValue());
        assertEquals(workflowData,workflowDataCaptor.getValue());
        verify(metaDataMap,times(2)).put(anyString(),anyString());
        verifyAll();
    }
    @Test
    void executeWithInValidPayload() throws RepositoryException, WorkflowException {
        when(workItem.getMetaDataMap()).thenReturn(metaDataMap);
        contentApprovalPayloadValidatorProcess.execute(workItem,workflowSession,metaDataMap);
        verify(workflowSession,times(0)).updateWorkflowData(workflow,workflowData);
        verify(metaDataMap,times(1)).put(anyString(),anyString());
        verifyAll();
    }
    @Test
    void executeWithPayloadAndNoApprover() throws RepositoryException, WorkflowException {
        map.put("/content/global/merkle","approver1");
        //when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
        when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
        when(userManager.getAuthorizable("approver1")).thenReturn(null);
        when(workItem.getMetaDataMap()).thenReturn(metaDataMap);
        contentApprovalPayloadValidatorProcess.execute(workItem,workflowSession,metaDataMap);
        verify(workflowSession).updateWorkflowData(workflowCaptor.capture(),workflowDataCaptor.capture());
        assertEquals(workflow,workflowCaptor.getValue());
        assertEquals(workflowData,workflowDataCaptor.getValue());
        verify(metaDataMap).put(anyString(),anyString());
        verifyAll();
    }
    @Test
    void executeWithException() throws RepositoryException, WorkflowException {
        map.put("/content/global/merkle","approver1");
        //when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
        when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
        doThrow(new RepositoryException("exception")).when(userManager).getAuthorizable(anyString());
        contentApprovalPayloadValidatorProcess.execute(workItem,workflowSession,metaDataMap);
        verify(workflowSession,times(0)).updateWorkflowData(workflow,workflowData);
        verifyAll();
    }
    @Test
    void executeWithPayloadAndInValidResolver() throws RepositoryException, WorkflowException {
        map.put("/content/global/merkle","approver1");
       //when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
        when(workItem.getMetaDataMap()).thenReturn(metaDataMap);
        when(resolver.adaptTo(UserManager.class)).thenReturn(null);
        contentApprovalPayloadValidatorProcess.execute(workItem,workflowSession,metaDataMap);
        verify(workflowSession).updateWorkflowData(workflowCaptor.capture(),workflowDataCaptor.capture());
        assertEquals(workflow,workflowCaptor.getValue());
        assertEquals(workflowData,workflowDataCaptor.getValue());
        verify(metaDataMap).put(anyString(),anyString());
        verifyAll();
    }
    void verifyAll() {
        verifyNoMoreInteractions(workItem,workflowSession,workflow,workflowData,genericListService);
        verifyNoMoreInteractions(metaDataMap,userManager,authorizable,resolver);
    }
}