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

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.Response;

/**
 * Persistence service interface.
 */
public interface CommandService {

  /**
   * Execute a command.
   * 
   * @param <A> Action
   * @param <R> Response
   * @param action Action
   * @return Response
   */
  <A extends Action<R>, R extends Response> void execute(A action, AbstractAsyncCallback<R> callback);

  /**
   * Execute a command with the next request.
   * 
   * @param <A> Action
   * @param <R> Response
   * @param action Action
   * @return Response
   */
  <A extends Action<R>, R extends Response> void executeLater(A action,
      AbstractAsyncCallback<R> callback);

  /**
   * Deletes all the cached actions.
   */
  void deleteCache();
}
