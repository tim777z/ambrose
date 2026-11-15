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
package com.twitter.ambrose.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link WorkflowSummary}.
 */
public class WorkflowSummaryTest {

  private WorkflowSummary summary;

  @Before
  public void setUp() {
    summary = new WorkflowSummary("test_id", "test_user", "test_workflow", WorkflowSummary.Status.RUNNING, 50, System.currentTimeMillis());
  }

  @Test
  public void constructor_setsAllFields() {
    assertNotNull("WorkflowSummary should be instantiable", summary);
    assertEquals("id should match", "test_id", summary.getId());
    assertEquals("userId should match", "test_user", summary.getUserId());
    assertEquals("name should match", "test_workflow", summary.getName());
    assertEquals("status should match", WorkflowSummary.Status.RUNNING, summary.getStatus());
    assertEquals("progress should match", 50, summary.getProgress());
    assertTrue("createdAt should be set", summary.getCreatedAt() > 0);
  }

  @Test
  public void getterSetter_forId() {
    summary.setId("new_id");
    assertEquals("id should be updatable", "new_id", summary.getId());
  }

  @Test
  public void getterSetter_forProgress() {
    summary.setProgress(100);
    assertEquals("progress should be updatable", 100, summary.getProgress());
  }

  @Test
  public void getterSetter_forStatus() {
    summary.setStatus(WorkflowSummary.Status.FAILED);
    assertEquals("status should be updatable", WorkflowSummary.Status.FAILED, summary.getStatus());
  }

  @Test
  public void defaultConstructor_createsInstance() {
    WorkflowSummary empty = new WorkflowSummary();
    assertNotNull("Default constructor should create instance", empty);
  }
}