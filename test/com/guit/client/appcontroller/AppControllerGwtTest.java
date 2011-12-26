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
package com.guit.client.appcontroller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.junit.client.GWTTestCase;

import com.guit.client.GuitController;
import com.guit.client.binder.GuitBinder;
import com.guit.client.command.CommandService;
import com.guit.client.command.CommandServiceImpl;
import com.guit.client.place.PlaceManager;
import com.guit.client.place.PlaceManagerImpl;

public class AppControllerGwtTest extends GWTTestCase {

  private static Controller controller;

  public static class Controller extends GuitController<ControllerBinder> {

    @Override
    protected void initialize() {
      controller = this;
    }

    public EventBus getEventBus() {
      return eventBus;
    }

    public PlaceManager getPlaceManager() {
      return placeManager;
    }

    public CommandService getCommandService() {
      return commandService;
    }
  }

  public static interface ControllerBinder extends GuitBinder<Controller> {
  }

  private AppController appController;

  @Override
  public String getModuleName() {
    return "com.guit.AppControllerModule";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    appController = GWT.create(AppController.class);
    appController.inject();
  }

  public void testControllerWasCreated() {
    assertNotNull(controller);
  }

  public void testStaticAccessToSingletons() {
    assertEquals(appController.getEventBus(), controller.getEventBus());
    assertEquals(appController.getPlaceManager(), controller.getPlaceManager());
    assertEquals(appController.getCommandService(), controller.getCommandService());
  }

  public void testUsesImplementations() {
    assertTrue(controller.getEventBus() instanceof SimpleEventBus);
    assertTrue(controller.getPlaceManager() instanceof PlaceManagerImpl);
    assertTrue(controller.getCommandService() instanceof CommandServiceImpl);
  }
}
