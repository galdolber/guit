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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.junit.GWTMockUtilities;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import com.guit.client.GuitEntryPoint;
import com.guit.client.appcontroller.AppController;
import com.guit.client.command.CommandService;
import com.guit.client.place.PlaceManager;

public class GuitTestHelper implements AppController {

  private static class TestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
      bind(PlaceManager.class).to(PlaceManagerMock.class).in(Singleton.class);
      bind(CommandService.class).to(CommandServiceMock.class).in(Singleton.class);
      bindListener(Matchers.any(), new GuitTestListener());
    }
  }

  private final Injector injector;

  private final Module module;

  public GuitTestHelper(Module module) {
    this.module = module;
    GWTMockUtilities.disarm();
    injector = Guice.createInjector(new TestModule(), module);
  }

  public void setUp() {
    GuitEntryPoint.setAppController(this);
    injector.injectMembers(module);
  }

  public void tearDown() {
  }

  public <T> T get(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  @Override
  public CommandService getCommandService() {
    return get(CommandService.class);
  }

  @Override
  public EventBus getEventBus() {
    return get(EventBus.class);
  }

  @Override
  public PlaceManager getPlaceManager() {
    return get(PlaceManager.class);
  }

  @Override
  public void inject() {
    // DO NOTHING
  }
}
