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

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import com.guit.client.binder.GuitBinder;
import com.guit.client.command.CommandService;
import com.guit.client.place.PlaceManager;

/**
 * Abstract presenter with EventBus, CommandService and PlaceManager support.
 * 
 * @param <B> Binder class.
 */
@SuppressWarnings("rawtypes")
public abstract class GuitPresenter<B extends GuitBinder> extends GuitPresenterBase<B> {

  protected EventBus eventBus;
  protected CommandService commandService;
  protected PlaceManager placeManager;

  @Inject
  @SuppressWarnings("unchecked")
  public void inject(EventBus eventBus, CommandService commandService, PlaceManager placeManager,
      B binder) {
    this.eventBus = eventBus;
    this.commandService = commandService;
    this.placeManager = placeManager;
    this.binder = binder;
    binder.bind(this, eventBus);

    initialize();
  }
}
