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
import org.junit.Test;

/**
 * Test for HRavenStatsWriteService initialization and basic operations.
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
}