/*
 * Copyright 2010 Gal Dolber.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.client.command;

/**
 * Represents a method on a guit service to be called.
 * 
 * @param <R> The return type of the method
 */
public interface AsyncMethod<R> {

  /**
   * Fires the method.
   * 
   * @param async Async callback handler
   */
  void fire(Async<R> async);
}
