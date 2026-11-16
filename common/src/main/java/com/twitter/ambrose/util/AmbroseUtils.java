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
package com.twitter.ambrose.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.twitter.ambrose.model.Event;
import com.twitter.ambrose.service.StatsWriteService;

public final class AmbroseUtils {

  private static final Logger log = LoggerFactory.getLogger(AmbroseUtils.class);

  private AmbroseUtils() {}

  /**
   * Handle and ignore any IOException while sending DagNodeNameMap to statsWriteService
   * @param statsWriteService to send DagNodeNameMap to
   * @param scriptId unique id of the script (must not be null)
   * @param dagNodeNameMap map of name to dag node
   */
  public static void sendDagNodeNameMap(StatsWriteService statsWriteService, String scriptId, Map dagNodeNameMap) {
    if (scriptId == null) {
      log.error("scriptId must not be null when sending dag node map");
      return;
    }
    try {
      statsWriteService.sendDagNodeNameMap(scriptId, dagNodeNameMap);
    } catch (IOException e) {
      log.error("Couldn't send dag to StatsWriteService for scriptId: {}", scriptId, e);
    }
  }

  /**
   * Handle and ignore any IOException while sending Event to statsWriteService
   * @param statsWriteService to send event to
   * @param scriptId unique id of the script (must not be null)
   * @param event ambrose event
   */
  public static void pushEvent(StatsWriteService statsWriteService, String scriptId, Event event) {
    if (scriptId == null) {
      log.error("scriptId must not be null when pushing event");
      return;
    }
    try {
      statsWriteService.pushEvent(scriptId, event);
    } catch (IOException e) {
      log.error("Couldn't send event to StatsWriteService for scriptId: {}", scriptId, e);
    }
  }

  /**
   * Send workflow progress event
   * @param statsWriteService
   * @param scriptId
   * @param progress
   */
  public static void pushWorkflowProgressEvent(StatsWriteService statsWriteService, String scriptId, int progress) {
    Map<Event.WorkflowProgressField, String> eventData = Maps.newHashMap();
    eventData.put(Event.WorkflowProgressField.workflowProgress, Integer.toString(progress));
    pushEvent(statsWriteService, scriptId, new Event.WorkflowProgressEvent(eventData));
  }
}