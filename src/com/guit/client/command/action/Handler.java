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
package com.guit.client.command.action;

/**
 * Rpc handler.
 * 
 * @param <A> Action
 * @param <R> Response
 */
public interface Handler<A extends Action<R>, R extends Response> {

  /**
   * Action handler.
   * 
   * @param action Action
   * @return Response
   * @throws Exception RpcException
   */
  R handle(A action) throws CommandException;
}
