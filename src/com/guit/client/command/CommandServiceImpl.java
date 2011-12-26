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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommandServiceImpl implements CommandService, ScheduledCommand {

  private final EventBus eventBus;
  private final CommandRpcAsync service;

  private HashMap<Action<Response>, ArrayList<AsyncCallback<Response>>> actionsMap =
      new LinkedHashMap<Action<Response>, ArrayList<AsyncCallback<Response>>>();

  // Requests pools
  private final ArrayList<CommandBatchRequestUnit> singleRequestPool =
      new ArrayList<CommandBatchRequestUnit>();
  private final ArrayList<CommandSingleRequestUnit> batchRequestPool =
      new ArrayList<CommandSingleRequestUnit>();

  // Cache
  private final HashMap<Action<Response>, Response> cache =
      new HashMap<Action<Response>, Response>();

  private boolean schedulerActive = false;

  @Inject
  public CommandServiceImpl(EventBus eventBus, CommandRpcAsync service) {
    this.eventBus = eventBus;
    this.service = service;
  }

  @Override
  public <A extends Action<R>, R extends Response> void execute(A action,
      AbstractAsyncCallback<R> callback) {
    addToBatch(action, callback);

    if (!schedulerActive) {
      Scheduler.get().scheduleDeferred(this);
      schedulerActive = true;
    }
  }

  @SuppressWarnings("unchecked")
  public <A, R> void addToBatch(A action, AsyncCallback<R> callback) {
    ArrayList<AsyncCallback<Response>> callbacks = actionsMap.get(action);
    if (callbacks == null) {
      callbacks = new ArrayList<AsyncCallback<Response>>();
    }
    callbacks.add((AsyncCallback<Response>) callback);
    actionsMap.put((Action<Response>) action, callbacks);
  }

  @Override
  public <A extends Action<R>, R extends Response> void executeLater(A action,
      AbstractAsyncCallback<R> callback) {
    addToBatch(action, callback);
  }

  /**
   * Get a single request unit.
   */
  private CommandBatchRequestUnit getBatchRequestUnit() {
    if (singleRequestPool.size() > 0) {
      CommandBatchRequestUnit poolInstance = singleRequestPool.get(0);
      singleRequestPool.remove(0);
      return poolInstance;
    } else {
      return new CommandBatchRequestUnit(cache, eventBus, service,
          new CommandRequestUnitFinish<CommandBatchRequestUnit>() {
            @Override
            public void onFinish(CommandBatchRequestUnit requestUnit) {
              singleRequestPool.add(requestUnit);
            }
          });
    }
  }

  /**
   * Get a batch request unit.
   */
  private CommandSingleRequestUnit getSingleRequestUnit() {
    if (batchRequestPool.size() > 0) {
      CommandSingleRequestUnit poolInstance = batchRequestPool.get(0);
      batchRequestPool.remove(0);
      return poolInstance;
    } else {
      return new CommandSingleRequestUnit(cache, eventBus, service,
          new CommandRequestUnitFinish<CommandSingleRequestUnit>() {
            @Override
            public void onFinish(CommandSingleRequestUnit requestUnit) {
              batchRequestPool.add(requestUnit);
            }
          });
    }
  }

  @Override
  public void deleteCache() {
    cache.clear();
  }

  @Override
  public void execute() {
    schedulerActive = false;
    if (!actionsMap.isEmpty()) {
      if (actionsMap.size() == 1) {
        getSingleRequestUnit().execute(actionsMap);
      } else {
        getBatchRequestUnit().execute(actionsMap);
      }
      actionsMap = new HashMap<Action<Response>, ArrayList<AsyncCallback<Response>>>();
    }
  }
}
