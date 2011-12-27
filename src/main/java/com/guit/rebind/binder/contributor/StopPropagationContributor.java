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
package com.guit.rebind.binder.contributor;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.StringSourceWriter;

import com.guit.rebind.binder.BinderContext;
import com.guit.rebind.binder.BinderContributor;

public class StopPropagationContributor implements BinderContributor {

  private static final String domEventsPackage = "com.google.gwt.event.dom.client";

  @Override
  public void contribute(BinderContext binderContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    JMethod m = binderContext.getMethod();
    JClassType eventType = binderContext.getEventType();
    JClassType presenterType = binderContext.getPresenterType();
    if (!eventType.getPackage().getName().equals(domEventsPackage)) {
      logger.log(Type.ERROR, String.format(
          "@StopPropagation annotation is only valid for dom events. Found: %s.%s", presenterType
              .getQualifiedSourceName(), m.getName()));
      throw new UnableToCompleteException();
    }

    StringSourceWriter writer = new StringSourceWriter();
    writer.print("event.stopPropagation();");
    binderContext.addBeforeHandler(writer);
  }

  @Override
  public String name() {
    return "StopPropagation";
  }
}
