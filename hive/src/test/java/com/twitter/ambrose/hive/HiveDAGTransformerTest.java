/*
Copyright 2013, Lorand Bendig

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
package com.twitter.ambrose.hive;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.apache.hadoop.hive.ql.hooks.HookContext;
import org.apache.hadoop.hive.ql.plan.MapredWork;

/**
 * Test for HiveDAGTransformer core functionality.
 * Covers createNodeIdToDAGNode with a stub HookContext.
 */
public class HiveDAGTransformerTest {

  @Mock
  private HookContext hookContext;

  private HiveDAGTransformer transformer;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    // Create a minimal HookContext for testing
    transformer = new HiveDAGTransformer(hookContext);
  }

  @Test
  public void testTransformerInstantiation() {
    assertNotNull("Transformer should be instantiable", transformer);
  }

  @Test
  public void testGetNodeIdToDAGNodeWithMinimalContext() {
    // Test that createNodeIdToDAGNode handles empty task list
    Map<String, HiveDAGTransformer.DAGNode<Job>> nodeMap = transformer.getNodeIdToDAGNode();
    assertNotNull("nodeIdToDAGNode should return a map", nodeMap);
  }

  @Test
  public void testCreateNodeIdToDAGNodeWithEmptyTasks() {
    // When there are no tasks, nodeIdToDAGNode should be empty
    transformer = new HiveDAGTransformer(hookContext);
    Map<String, HiveDAGTransformer.DAGNode<Job>> nodeMap = transformer.getNodeIdToDAGNode();
    assertNotNull("nodeIdToDAGNode should return a map", nodeMap);
    assertTrue("nodeIdToDAGNode should be empty with no tasks", nodeMap.isEmpty());
  }
}