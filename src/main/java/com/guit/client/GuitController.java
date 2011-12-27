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
package com.guit.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import com.guit.client.binder.GuitBinder;
import com.guit.client.command.CommandService;
import com.guit.client.place.PlaceManager;

/**
 * Abstract controller with EventBus, CommandService and PlaceManager support.
 * 
 * @param <B> Binder class.
 */
@SuppressWarnings("rawtypes")
public abstract class GuitController<B extends GuitBinder> implements Controller {

  protected EventBus eventBus;
  protected CommandService commandService;
  protected PlaceManager placeManager;
  private B binder;

  protected Scheduler scheduler;

  /**
   * Get called after the binder, eventbus, commandservice and placemanager are
   * injected.
   */
  protected void initialize() {
  }

  @SuppressWarnings("unchecked")
  @Inject
  public void inject(EventBus eventBus, CommandService commandService, PlaceManager placeManager,
      Scheduler scheduler, B binder) {
    this.eventBus = eventBus;
    this.commandService = commandService;
    this.placeManager = placeManager;
    this.scheduler = scheduler;
    this.binder = binder;
    binder.bind(this, eventBus);

    // Let gin inject the class
    scheduler.scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        initialize();
      }
    });
  }

  @Override
  public void destroy() {
    binder.destroy();
  }
}
