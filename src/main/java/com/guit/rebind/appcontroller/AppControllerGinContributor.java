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
package com.guit.rebind.appcontroller;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.event.shared.EventBus;

import com.guit.client.command.CommandService;
import com.guit.client.ga.GoogleAnalyticsModule;
import com.guit.client.ga.GoogleAnalyticsTracker;
import com.guit.client.gin.Base64CrypterModule;
import com.guit.client.gin.CommandMockModule;
import com.guit.client.gin.CommandModule;
import com.guit.client.gin.EventBusModule;
import com.guit.client.gin.PlaceManagerMockModule;
import com.guit.client.gin.PlaceManagerModule;
import com.guit.client.gin.SchedulerModule;
import com.guit.client.place.PlaceManager;
import com.guit.rebind.gin.GinContext;
import com.guit.rebind.gin.GinContributor;

import java.util.List;

public class AppControllerGinContributor implements GinContributor {

  @Override
  public void collaborate(GinContext ginContext, TreeLogger logger, GeneratorContext context) {
    List<String> appControllers = findConfigurationProperty(context, "app.controller").getValues();
    List<String> ginModules = findConfigurationProperty(context, "app.gin.module").getValues();

    ginContext.addModule(SchedulerModule.class.getCanonicalName());
    ginContext.addModule(EventBusModule.class.getCanonicalName());
    for (String g : ginModules) {
      ginContext.addModule(g);
    }

    for (String c : appControllers) {
      ginContext.addInjectedType(c);
    }

    ginContext.addInjectedType(EventBus.class.getCanonicalName());
    ginContext.addInjectedType(CommandService.class.getCanonicalName());
    ginContext.addInjectedType(PlaceManager.class.getCanonicalName());

    if (findConfigurationProperty(context, "app.use.command").getValues().get(0).equals("true")) {
      ginContext.addModule(CommandModule.class.getCanonicalName());
    } else {
      ginContext.addModule(CommandMockModule.class.getCanonicalName());
    }

    if (findConfigurationProperty(context, "app.use.place").getValues().get(0).equals("true")) {
      ginContext.addModule(PlaceManagerModule.class.getCanonicalName());
    } else {
      ginContext.addModule(PlaceManagerMockModule.class.getCanonicalName());
    }

    if (!findConfigurationProperty(context, "app.google.analytics").getValues().get(0).isEmpty()) {
      ginContext.addModule(GoogleAnalyticsModule.class.getCanonicalName());
      ginContext.addInjectedType(GoogleAnalyticsTracker.class.getCanonicalName());
    }

    if (findConfigurationProperty(context, "app.encrypt.base64").getValues().get(0).equals("true")) {
      ginContext.addModule(Base64CrypterModule.class.getCanonicalName());
    }
  }

  private ConfigurationProperty findConfigurationProperty(GeneratorContext context, String property) {
    try {
      return context.getPropertyOracle().getConfigurationProperty(property);
    } catch (BadPropertyValueException e) {
      throw new IllegalStateException(e);
    }
  }
}
