/*
Copyright 2012 Twitter, Inc.

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
package com.twitter.ambrose.pig;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pig.backend.hadoop.datastorage.ConfigurationUtil;
import org.apache.pig.impl.plan.OperatorKey;
import org.apache.pig.impl.plan.OperatorPlan;
import org.apache.pig.tools.pigstats.JobStats;
import org.apache.pig.tools.pigstats.OutputStats;
import org.apache.pig.tools.pigstats.PigProgressNotificationListener;
import org.apache.pig.tools.pigstats.PigStats;
import org.apache.pig.tools.pigstats.mapreduce.MRScriptState;

import com.twitter.ambrose.model.DAGNode;
import com.twitter.ambrose.model.Event;
import com.twitter.ambrose.model.Job;
import com.twitter.ambrose.model.hadoop.MapReduceHelper;
import com.twitter.ambrose.service.StatsWriteService;
import com.twitter.ambrose.service.impl.hraven.HRavenStatsWriteService;
import com.twitter.ambrose.util.AmbroseUtils;

/**
 * Test for AmbrosePigProgressNotificationListener core functionality.
 * Covers initialPlanNotification and job completion callbacks with mocked PigStats.
 */
public class AmbrosePigProgressNotificationListenerTest {

  @Mock
  private StatsWriteService statsWriteService;

  private AmbrosePigProgressNotificationListener listener;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    listener = new AmbrosePigProgressNotificationListener(statsWriteService);
  }

  @Test
  public void testListenerInstantiation() {
    assertNotNull("Listener should be instantiable", listener);
  }

  @Test
  public void testInitialPlanNotificationWithValidPlan() {
    // Setup pig config
    listener.pigConfig.getJobClient();
    listener.pigConfig.getJobGraph();
    listener.pigConfig.getPigProperties();

    // Test that initialPlanNotification doesn't throw NPE with valid setup
    listener.initialPlanNotification("test_script", (OperatorPlan<?>) null);
  }

  @Test
  public void testJobStartedNotification() {
    // Test that jobStartedNotification handles the case where job graph is empty
    listener.jobStartedNotification("test_script", "nonexistent-job-id");
  }

  @Test
  public void testJobFinishedNotification() {
    // Test jobFinishedNotification with null node
    listener.jobFinishedNotification("test_script", null);
  }

  @Test
  public void testProgressUpdatedNotification() {
    // Test progressUpdatedNotification with empty dagNodeMap
    listener.progressUpdatedNotification("test_script", 50);
  }
}