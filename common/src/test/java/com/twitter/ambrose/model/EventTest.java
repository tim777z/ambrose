/*
 * Copyright 2014 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.ambrose.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

public class EventTest {

  private Event<WorkflowSummary> workflowProgressEvent;

  @Before
  public void setUp() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("workflowProgress", "50");
    workflowProgressEvent = new Event.WorkflowProgressEvent(payload);
  }

  @Test
  public void testWorkflowProgressEventCreation() {
    assertNotNull("WorkflowProgressEvent should be instantiable", workflowProgressEvent);
    assertEquals("event type should be WORKFLOW_PROGRESS",
        Event.Type.WORKFLOW_PROGRESS, workflowProgressEvent.getType());
  }

  @Test
  public void testWorkflowProgressEventJsonRoundTrip() throws Exception {
    String asJson = workflowProgressEvent.toJson();
    Event<WorkflowSummary> roundTrip = Event.fromJson(asJson);
    assertNotNull("Round-tripped event should not be null", roundTrip);
    assertEquals("event type should match", Event.Type.WORKFLOW_PROGRESS, roundTrip.getType());
    assertEquals("workflow progress should match", "50",
        roundTrip.getPayload().get("workflowProgress"));
  }

  @Test
  public void testJobStartedEventJsonRoundTrip() throws Exception {
    DAGNode<Job> node = new DAGNode<Job>("test-node", new Job());
    node.getJob().setId("job-123");
    Event<DAGNode<? extends Job>> jobStarted = new Event.JobStartedEvent(node);
    String asJson = jobStarted.toJson();
    Event<DAGNode<? extends Job>> roundTrip = Event.fromJson(asJson);
    assertNotNull("Round-tripped event should not be null", roundTrip);
    assertEquals("event type should be JOB_STARTED",
        Event.Type.JOB_STARTED, roundTrip.getType());
    assertNotNull("payload should not be null", roundTrip.getPayload());
    assertEquals("payload node name should match", "test-node",
        roundTrip.getPayload().getName());
  }

  @Test
  public void testDefaultConstructorCreatesInstance() {
    Event<?> empty = new Event<>();
    assertNotNull("Default constructor should create instance", empty);
  }

  @Test
  public void testEventPayloadValidation() {
    // Test that events can be created with various payload types
    Event<String> stringEvent = new Event<>("test-key", Event.Type.JOB_STARTED, System.currentTimeMillis(), "test-payload");
    assertNotNull("Event with string payload should be instantiable", stringEvent);
    assertEquals("payload should match", "test-payload", stringEvent.getPayload());

    Event<Integer> intEvent = new Event<>("test-key", Event.Type.JOB_STARTED, System.currentTimeMillis(), 42);
    assertNotNull("Event with int payload should be instantiable", intEvent);
    assertEquals("payload should match", 42, intEvent.getPayload());
  }
}