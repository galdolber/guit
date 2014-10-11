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

import com.google.gwt.core.ext.CachedGeneratorResult;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.RebindMode;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.client.command.CommandService;
import com.guit.client.ga.GoogleAnalyticsTracker;
import com.guit.client.place.PlaceManager;
import com.guit.rebind.common.AbstractGenerator;
import com.guit.rebind.gin.GinInjectorGenerator;
import com.guit.rebind.gin.GinOracle;

import java.util.Date;
import java.util.List;

public class AppControllerGenerator extends AbstractGenerator {

  private final GinInjectorGenerator ginInjectorGenerator = new GinInjectorGenerator();

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    ginInjectorGenerator.generate(logger, context, null);

    List<String> appControllers = getConfigurationProperty("app.controller")
        .getValues();

    writer.println("public void inject() {");
    writer.indent();

    for (String c : appControllers) {
      writer.println(GinOracle.getInjectedInstance(c) + ";");
    }

    // Force eager instantiation of place manager
    writer.println("getPlaceManager();");

    // Google analytics setup
    String googleAnalyticsUa = getConfigurationProperty("app.google.analytics")
        .getValues().get(0);
    if (!googleAnalyticsUa.isEmpty()) {
      writer.println(GinOracle.getInjectedInstance(GoogleAnalyticsTracker.class
          .getCanonicalName()) + ".setAccount(\"" + googleAnalyticsUa + "\");");
    }

    // Fire current history state on load?
    if (getConfigurationProperty("app.fire.current.history.state").getValues()
        .get(0).equals("true")) {
      writer.println(History.class.getCanonicalName()
          + ".fireCurrentHistoryState();");
    }

    writer.outdent();
    writer.println("}");

    writer.println("public " + PlaceManager.class.getCanonicalName()
        + " getPlaceManager() {");
    writer.indent();
    writer.print("return "
        + GinOracle.getInjectedInstance(PlaceManager.class.getCanonicalName())
        + ";");
    writer.outdent();
    writer.println("}");

    writer.println("public " + CommandService.class.getCanonicalName()
        + " getCommandService() {");
    writer.indent();
    writer
        .print("return "
            + GinOracle.getInjectedInstance(CommandService.class
                .getCanonicalName()) + ";");
    writer.outdent();
    writer.println("}");

    writer.println("public " + EventBus.class.getCanonicalName()
        + " getEventBus() {");
    writer.indent();
    writer.println("return "
        + GinOracle.getInjectedInstance(EventBus.class.getCanonicalName())
        + ";");
    writer.outdent();
    writer.println("}");
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImplementedInterface(typeName);
  }
  
  @Override
  protected RebindMode rebindMode() {
    CachedGeneratorResult last = context.getCachedGeneratorResult();
    if (last != null) {
      return RebindMode.USE_ALL_CACHED;
    }
    return RebindMode.USE_ALL_NEW;
  }
}
