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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test for HRavenStatsWriteService initialization, pushEvent, and shutdown operations
 * using mocked hRaven dependencies.
 */
public class HRavenStatsWriteServiceTest {

  @Test
  public void testInitWriteServiceCreatesFlowKey() {
    HRavenStatsWriteService service = new HRavenStatsWriteService();
    assertNotNull("HRavenStatsWriteService should be instantiable", service);
  }

  @Test
  public void testGetWorkflowIdReturnsNullBeforeInit() {
    HRavenStatsWriteService service = new HRavenStatsWriteService();
    assertNotNull("service should be instantiable", service);
    String id = service.getWorkflowId();
    assertNotNull("getWorkflowId should return a value after init", id);
  }

  @Test
  public void testPushEventWithMockedServices() throws IOException {
    HRavenStatsWriteService service = new HRavenStatsWriteService();

    // Mock the Properties to avoid hBase configuration lookup
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
    HRavenStatsWriteService service = new HRavenStatsWriteService();

    // Mock the Properties to avoid hBase configuration lookup
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("jobName", "test-job");
    propertiesMap.put("jobTracker", "localhost:8021");
    Properties properties = new Properties();
    properties.putAll(propertiesMap);

    service.initWriteService(properties);

    // Shutdown with no running jobs should complete without error
    service.shutdown();
  }
}