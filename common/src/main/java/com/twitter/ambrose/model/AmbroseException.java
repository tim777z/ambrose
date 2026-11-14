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
 * Base checked exception for Ambrose-related errors.
 * <p>
 * Use this exception type for domain-specific error conditions
 * that callers should explicitly handle rather than letting
 * runtime exceptions propagate unbounded.
 */
public class AmbroseException extends Exception {

  private static final long serialVersionUID = 1L;

  public AmbroseException() {
    super();
  }

  public AmbroseException(String message) {
    super(message);
  }

  public AmbroseException(String message, Throwable cause) {
    super(message, cause);
  }

  public AmbroseException(Throwable cause) {
    super(cause);
  }
}