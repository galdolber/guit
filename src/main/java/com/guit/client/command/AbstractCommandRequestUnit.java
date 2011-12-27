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

import com.google.gwt.event.shared.EventBus;

import com.guit.client.command.action.Action;
import com.guit.client.command.action.Response;

import java.util.HashMap;

public class AbstractCommandRequestUnit {

  protected final EventBus eventBus;
  protected final CommandRpcAsync service;
  protected final HashMap<Action<Response>, Response> cache;

  public AbstractCommandRequestUnit(HashMap<Action<Response>, Response> cache, EventBus eventBus,
      CommandRpcAsync service) {
    this.cache = cache;
    this.eventBus = eventBus;
    this.service = service;
  }
}
