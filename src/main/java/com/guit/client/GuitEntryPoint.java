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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;

import com.guit.client.appcontroller.AppController;
import com.guit.client.command.CommandService;
import com.guit.client.place.PlaceManager;

/**
 * Guit entry point and static gateway to EventBus, PlaceManager and
 * CommandService.
 */
public class GuitEntryPoint implements EntryPoint {

  private static AppController appController;

  /**
   * @return The command service.
   */
  public static CommandService getCommandService() {
    return appController.getCommandService();
  }

  /**
   * @return The event bus.
   */
  public static EventBus getEventBus() {
    return appController.getEventBus();
  }

  /**
   * @return The place manager.
   */
  public static PlaceManager getPlaceManager() {
    return appController.getPlaceManager();
  }

  /**
   * Public for testing internal use.
   */
  public static void setAppController(AppController appController) {
    GuitEntryPoint.appController = appController;
  }

  public GuitEntryPoint() {
    setAppController(GWT.<AppController> create(AppController.class));
  }

  @Override
  public void onModuleLoad() {
    appController.inject();
  }
}
