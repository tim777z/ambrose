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
package com.twitter.ambrose.hive;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.twitter.ambrose.model.DAGNode;
import com.twitter.ambrose.model.Job;
import com.twitter.hraven.hooks.HookContext;

import org.apache.hadoop.hive.ql.QueryPlan;
import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.plan.MapredWork;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link HiveDAGTransformer}.
 * <p>
 * Tests the DAG node creation logic using a mocked HookContext,
 * without requiring a running Hive instance.
 */
@RunWith(MockitoJUnitRunner.class)
public class HiveDAGTransformerTest {

  @Mock
  private HookContext hookContext;

  private HiveDAGTransformer transformer;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(hookContext.getConf()).thenReturn(new org.apache.hadoop.conf.Configuration());
    when(hookContext.getQueryPlan()).thenReturn(null);
    transformer = new HiveDAGTransformer(hookContext);
  }

  @Test
  public void constructor_withValidHookContext_initializesWithoutException() {
    // when/then - constructor should not throw
    // The constructor may call initClosure which has a RuntimeException
    // for incompatible Hive APIs, but with a mock config it should complete.
    assertNotNull("Transformer should be created", transformer);
  }

  @Test
  public void getNodeIdToDAGNode_withEmptyTasks_returnsEmptyMap() {
    // when
    Map<String, DAGNode<Job>> nodeIdToDAGNode = transformer.getNodeIdToDAGNode();

    // then
    assertNotNull("Should return a map", nodeIdToDAGNode);
    assertTrue("Map should be empty when no tasks", nodeIdToDAGNode.isEmpty());
  }

  @Test
  public void createNodeIdToDAGNode_withMockedTasks_populatesMap() throws Exception {
    // Given - set up a mock query plan with tasks
    // This test verifies the createNodeIdToDAGNode logic path
    // with a stubbed HookContext that returns tasks.
    // Full integration requires a running Hive instance with query plans.
    QueryPlan mockQueryPlan = mock(QueryPlan.class);
    Task<MapredWork> mockTask = mock(Task.class);
    MapredWork mockMapWork = mock(MapredWork.class);

    when(mockTask.getWork()).thenReturn(mockMapWork);
    when(mockQueryPlan.getRootTasks()).thenReturn(Arrays.asList(mockTask));

    when(hookContext.getQueryPlan()).thenReturn(mockQueryPlan);

    // When
    transformer = new HiveDAGTransformer(hookContext);

    // Then
    Map<String, DAGNode<Job>> nodeIdToDAGNode = transformer.getNodeIdToDAGNode();
    assertNotNull("Should return a map", nodeIdToDAGNode);
    assertTrue("Map should contain the mocked task", !nodeIdToDAGNode.isEmpty());
  }
}