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

/**
 * Typed runtime exception raised by Ambrose service-layer components
 * (e.g. {@code StatsWriteService} implementations) when an operation
 * fails. Extends {@link RuntimeException} so callers are not forced
 * to declare checked exceptions, while still providing a distinct,
 * identifiable type for error handling and logging.
 */
public class AmbroseServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AmbroseServiceException() {
    super();
  }

  public AmbroseServiceException(String message) {
    super(message);
  }

  public AmbroseServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public AmbroseServiceException(Throwable cause) {
    super(cause);
  }
}
