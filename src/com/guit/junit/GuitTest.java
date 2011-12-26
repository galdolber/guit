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
import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import com.guit.client.command.CommandService;
import com.guit.client.place.PlaceManager;

import org.junit.After;
import org.junit.Before;

import java.lang.reflect.Proxy;

public abstract class GuitTest extends AbstractModule {

  protected final GuitTestHelper helper = new GuitTestHelper(this);

  protected final EventBus eventBus = helper.getEventBus();

  protected final CommandService commandService = helper.getCommandService();

  protected final PlaceManager placeManager = helper.getPlaceManager();

  @SuppressWarnings("unchecked")
  protected <D> void mock(final Class<D> clazz) {
    bind(clazz).toProvider(new Provider<D>() {
      @Override
      public D get() {
        return (D) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
            new PojoInvocationHandler(clazz));
      }
    });
  }

  @SuppressWarnings("unchecked")
  protected <D> void mockSingleton(final Class<D> clazz) {
    bind(clazz).toInstance(
        (D) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
            new PojoInvocationHandler(clazz)));
  }

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
}
