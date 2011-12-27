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
package com.guit.junit;

import com.google.inject.Inject;
import com.google.inject.Injector;

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.command.CommandService;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.ActionHandler;
import com.guit.client.command.action.CommandException;
import com.guit.client.command.action.Handler;
import com.guit.client.command.action.Response;

public class CommandServiceMock implements CommandService {

  @Inject
  Injector injector;

  @Override
  @SuppressWarnings("unchecked")
  public <A extends Action<R>, R extends Response> void execute(A action,
      AbstractAsyncCallback<R> callback) {
    Class<? extends Action<R>> actionClass = (Class<? extends Action<R>>) action.getClass();
    if (actionClass.isAnnotationPresent(ActionHandler.class)) {
      Class<? extends Handler<A, R>> handlerClass =
          (Class<? extends Handler<A, R>>) findClass(actionClass.getAnnotation(ActionHandler.class)
              .value());
      Handler<A, R> handler = injector.getInstance(handlerClass);
      try {
        callback.onSuccess(handler.handle(action));
      } catch (CommandException caught) {
        callback.onFailure(caught);
      }
    } else {
      throw new IllegalStateException("The action " + actionClass.getCanonicalName()
          + " do not specify the @ActionHandler");
    }
  }

  @Override
  public <A extends Action<R>, R extends Response> void executeLater(A action,
      AbstractAsyncCallback<R> callback) {
    execute(action, callback);
  }

  private Class<?> findClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Class not found. Found: " + className);
    }
  }

  @Override
  public void deleteCache() {
  }
}
