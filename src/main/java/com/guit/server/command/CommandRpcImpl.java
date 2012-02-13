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
package com.guit.server.command;

import com.google.inject.Inject;
import com.google.inject.Injector;

import com.guit.client.command.CommandRpc;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.ActionHandler;
import com.guit.client.command.action.BatchResponse;
import com.guit.client.command.action.CommandException;
import com.guit.client.command.action.Handler;
import com.guit.client.command.action.Response;

import java.util.ArrayList;

/**
 * Service implementation.
 */
public class CommandRpcImpl implements CommandRpc {

  @Inject
  Injector injector;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Response execute(Action<Response> action) throws CommandException {
    CommandHook commandHook = injector.getInstance(CommandHook.class);

    Class<? extends Action> actionClass = action.getClass();
    if (!actionClass.isAnnotationPresent(ActionHandler.class)) {
      throw new IllegalStateException("The action " + actionClass.getCanonicalName()
          + " don't have the RpcActionHandler annotation");
    }
    try {
      String handlerClassName = actionClass.getAnnotation(ActionHandler.class).value();
      Class<?> handlerClass = findClass(handlerClassName);
      Handler<Action<Response>, Response> rpcHandler =
          (Handler<Action<Response>, Response>) injector.getInstance(handlerClass);

      Response response = rpcHandler.handle(action);

      if (commandHook != null) {
        commandHook.success(action, response);
      }

      return response;
    } catch (CommandException e) {
      if (commandHook != null) {
        commandHook.exception(action, e);
      }
      throw e;
    }
  }

  @Override
  public BatchResponse executeBatch(ArrayList<Action<Response>> actions) {
    BatchResponse batch = new BatchResponse();
    for (Action<Response> action : actions) {
      try {
        batch.add(execute(action));
      } catch (CommandException e) {
        batch.add(e);
      }
    }
    return batch;
  }

  private Class<?> findClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Class not found. Found: " + className);
    }
  }
}
