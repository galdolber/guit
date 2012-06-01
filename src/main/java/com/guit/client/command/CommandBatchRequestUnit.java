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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;

import com.guit.client.async.event.AsyncExceptionEvent;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.BatchResponse;
import com.guit.client.command.action.Cacheable;
import com.guit.client.command.action.CommandException;
import com.guit.client.command.action.CommandSerializable;
import com.guit.client.command.action.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandBatchRequestUnit extends AbstractCommandRequestUnit implements
    AsyncCallback<BatchResponse> {

  private final CommandRequestUnitFinish<CommandBatchRequestUnit> finishCommand;

  private ArrayList<Action<Response>> actions;

  private final Logger logger = Logger.getLogger("CommandService");

  private HashMap<Action<Response>, ArrayList<AsyncCallback<Response>>> actionsMap;

  public CommandBatchRequestUnit(HashMap<Action<Response>, Response> cache, EventBus eventBus,
      CommandRpcAsync service, CommandRequestUnitFinish<CommandBatchRequestUnit> finishCommand) {
    super(cache, eventBus, service);
    this.finishCommand = finishCommand;
  }

  public void execute(HashMap<Action<Response>, ArrayList<AsyncCallback<Response>>> actionsMap) {
    this.actionsMap = actionsMap;
    Set<Action<Response>> a = actionsMap.keySet();
    Iterator<Action<Response>> iterator = a.iterator();
    while (iterator.hasNext()) {
      Action<Response> action = iterator.next();
      if (LogConfiguration.loggingIsEnabled()) {
        logger.info("Executing action: " + action.toString());
      }

      if (action instanceof Cacheable && cache.containsKey(action)) {
        // Invoke callback
        Response response = cache.get(action);
        if (LogConfiguration.loggingIsEnabled()) {
          logger.info("Response from cache: " + response.toString());
        }
        ArrayList<AsyncCallback<Response>> actionCallbacks = actionsMap.get(action);
        for (AsyncCallback<Response> callback : actionCallbacks) {
          callback.onSuccess(response);
        }

        // Remove action and callback from the request
        // actionsMap.remove(action);
        iterator.remove();
      }
    }
    actions = new ArrayList<Action<Response>>(a);
    service.executeBatch(actions, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.log(Level.SEVERE, "Error executing action", caught);
    }

    if (!GWT.isScript()) {
      if (caught instanceof SerializationException) {
        StringBuilder sb = new StringBuilder();
        for (Action<Response> a : actions) {
          if (sb.length() > 0) {
            sb.append(" or ");
          }
          sb.append(a.getClass().getName());
        }
        throw new RuntimeException(
            "Some of your actions or responses contains non-serializable data. Found: "
                + sb.toString(), caught);
      }
    }

    eventBus.fireEvent(new AsyncExceptionEvent(caught));
    finishCommand.onFinish(CommandBatchRequestUnit.this);
  }

  @Override
  public void onSuccess(BatchResponse result) {
    ArrayList<CommandSerializable> responses = result.getResponses();
    for (int n = 0; n < responses.size(); n++) {
      CommandSerializable response = responses.get(n);

      ArrayList<AsyncCallback<Response>> callbacks = actionsMap.get(actions.get(n));
      if (response instanceof Response) {
        Action<Response> action = actions.get(n);
        if (action instanceof Cacheable) {
          if (LogConfiguration.loggingIsEnabled()) {
            logger.info("Caching action: " + action + ", response: " + response);
          }
          cache.put(action, (Response) response);
        }

        if (LogConfiguration.loggingIsEnabled()) {
          logger.info("Response received: " + response.toString());
        }
        for (AsyncCallback<Response> callback : callbacks) {
          callback.onSuccess((Response) response);
        }
      } else {
        if (LogConfiguration.loggingIsEnabled()) {
          logger.info("Exception received: " + response.toString());
        }
        eventBus.fireEvent(new AsyncExceptionEvent((CommandException) response));
        for (AsyncCallback<Response> callback : callbacks) {
          callback.onFailure((Throwable) response);
        }
      }
    }
    finishCommand.onFinish(CommandBatchRequestUnit.this);
  }
}
