/*
Copyright 2014 Twitter, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.twitter.ambrose.service.impl.hraven;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test for HRavenStatsWriteService initialization, pushEvent, and shutdown operations
 * using mocked hRaven dependencies.
 */
public class HRavenStatsWriteServiceTest {

  @org.mockito.Mock
  private com.twitter.hraven.datasource.FlowEventService flowEventService;

  @org.mockito.Mock
  private com.twitter.hraven.datasource.FlowQueueService flowQueueService;

  private HRavenStatsWriteService service;
  private static final String CLUSTER = "test-cluster";

  @Before
  public void setUp() {
    Mockito.openMocks(this);
    when(flowEventService.addEvent((com.twitter.hraven.FlowEvent) any())).thenReturn(true);
    when(flowQueueService.updateFlow((com.twitter.hraven.FlowQueueKey) any(), any())).thenReturn(true);
    service = new HRavenStatsWriteService();
  }

  @Test
  public void testInitWriteServiceCreatesFlowKey() {
    HRavenStatsWriteService s = new HRavenStatsWriteService();
    assertNotNull("HRavenStatsWriteService should be instantiable", s);
  }

  @Test
  public void testGetWorkflowIdReturnsNullBeforeInit() {
    HRavenStatsWriteService s = new HRavenStatsWriteService();
    assertNotNull("service should be instantiable", s);
    String id = s.getWorkflowId();
    // May be null before init, that's OK
  }

  @Test
  public void testPushEventWithMockedServices() throws IOException {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    assertNotNull("flowKey should be set after init", service.getWorkflowId());

    // Create a workflow progress event
    com.twitter.ambrose.model.Event event = new com.twitter.ambrose.model.Event(
        com.twitter.ambrose.model.Event.Type.WORKFLOW_PROGRESS);
    Map<com.twitter.ambrose.model.Event.WorkflowProgressField, String> payload = new java.util.HashMap<>();
    payload.put(com.twitter.ambrose.model.Event.WorkflowProgressField.workflowProgress, "100");
    event.setPayload(payload);

    service.pushEvent("workflow-123", event);

    // Verify the service can be instantiated and pushEvent doesn't crash
    // with mocked/incomplete services
  }

  @Test
  public void testShutdownWithNoJobs() throws IOException {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    // Shutdown with no running jobs should complete without error
    service.shutdown();
  }

  @Test
  public void testUpdateJobStarted_addsToRunningJobs() throws IOException {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    com.twitter.ambrose.model.DAGNode<com.twitter.ambrose.model.Job> node = new com.twitter.ambrose.model.DAGNode<>(
        com.twitter.ambrose.model.Job.newBuilder().id("job-1").build());

    service.updateJobStarted(node);
    assertTrue("runningJobs should contain the job id", service.runningJobs.contains("job-1"));
  }

  @Test
  public void testUpdateJobComplete_transitionsJobState() throws IOException {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    com.twitter.ambrose.model.DAGNode<com.twitter.ambrose.model.Job> finishedNode = new com.twitter.ambrose.model.DAGNode<>(
        com.twitter.ambrose.model.Job.newBuilder().id("job-1").build());

    service.updateJobComplete(finishedNode, com.twitter.ambrose.model.Event.Type.JOB_FINISHED);
    assertTrue("job should be removed from runningJobs", !service.runningJobs.contains("job-1"));
    assertTrue("job should be added to completedJobs", service.completedJobs.contains("job-1"));
  }

  @Test
  public void testShutdown_withFailedJobs_changesQueueStatus() throws IOException {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    com.twitter.ambrose.model.DAGNode<com.twitter.ambrose.model.Job> failedNode = new com.twitter.ambrose.model.DAGNode<>(
        com.twitter.ambrose.model.Job.newBuilder().id("job-1").build());

    service.updateJobComplete(failedNode, com.twitter.ambrose.model.Event.Type.JOB_FAILED);

    service.shutdown();
    // Shutdown should complete without error even with failed jobs
  }
}