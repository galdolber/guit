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
import com.guit.client.command.action.Cacheable;
import com.guit.client.command.action.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandSingleRequestUnit extends AbstractCommandRequestUnit implements
    AsyncCallback<Response> {

  private final CommandRequestUnitFinish<CommandSingleRequestUnit> finishCommand;

  private Action<Response> action;

  private final Logger logger = Logger.getLogger("CommandService");

  private ArrayList<AsyncCallback<Response>> callbacks;

  public CommandSingleRequestUnit(HashMap<Action<Response>, Response> cache, EventBus eventBus,
      CommandRpcAsync service, CommandRequestUnitFinish<CommandSingleRequestUnit> finishCommand) {
    super(cache, eventBus, service);
    this.finishCommand = finishCommand;
  }

  public void execute(HashMap<Action<Response>, ArrayList<AsyncCallback<Response>>> actionsMap) {
    action = actionsMap.keySet().iterator().next();
    callbacks = actionsMap.get(action);
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("Executing action: " + action.toString());
    }
    if (action instanceof Cacheable && cache.containsKey(action)) {
      onCache(cache.get(action));
    } else {
      service.execute(action, this);
    }
  }

  @Override
  public void onFailure(Throwable caught) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.log(Level.SEVERE, "Error executing action", caught);
    }

    if (!GWT.isScript()) {
      if (caught instanceof SerializationException) {
        throw new RuntimeException(
            "Some of your actions or responses contains non-serializable data. Found: "
                + action.getClass().getName(), caught);
      }
    }

    eventBus.fireEvent(new AsyncExceptionEvent(caught));
    for (AsyncCallback<Response> callback : callbacks) {
      callback.onFailure(caught);
    }
    finishCommand.onFinish(CommandSingleRequestUnit.this);
  }

  @Override
  public void onSuccess(Response response) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("Response received: " + response.toString());
    }

    if (action instanceof Cacheable) {
      if (LogConfiguration.loggingIsEnabled()) {
        logger.info("Caching action: " + action + ", response: " + response);
      }
      cache.put(action, response);
    }

    process(response);
  }

  private void onCache(Response response) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("Response from cache: " + response.toString());
    }
    process(response);
  }

  private void process(Response response) {
    for (AsyncCallback<Response> callback : callbacks) {
      callback.onSuccess(response);
    }
    finishCommand.onFinish(CommandSingleRequestUnit.this);
  }
}
