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
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.rebind.StringSourceWriter;

import com.guit.client.binder.contributor.MouseButton;
import com.guit.client.binder.contributor.MouseButton.MouseButtons;
import com.guit.rebind.binder.BinderContext;
import com.guit.rebind.binder.BinderContributor;

public class MouseButtonContributor implements BinderContributor {

  @Override
  public void contribute(BinderContext binderContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    JClassType eventType = binderContext.getEventType();
    String eventName = eventType.getQualifiedSourceName();
    if (!eventName.equals(MouseDownEvent.class.getCanonicalName())
        && !eventName.equals(MouseUpEvent.class.getCanonicalName())) {
      logger.log(Type.ERROR, String.format(
          "The binder annotation %s is only valid in %s or %s events. Found: %s.%s",
          MouseButton.class.getCanonicalName(), MouseDownEvent.class.getCanonicalName(),
          MouseUpEvent.class.getCanonicalName(), binderContext.getPresenterType()
              .getQualifiedSourceName(), binderContext.getMethod().getName()));
      throw new UnableToCompleteException();
    }

    MouseButtons[] keycodes = binderContext.getMethod().getAnnotation(MouseButton.class).value();
    StringBuilder condition = new StringBuilder();
    for (MouseButtons button : keycodes) {
      if (condition.length() > 0) {
        condition.append(" && ");
      }

      // LEFT by default
      int buttonNumber = 1;
      switch (button) {
        case MIDDLE:
          buttonNumber = 4;
          break;
        case RIGHT:
          buttonNumber = 2;
          break;
        default:
          break;
      }
      condition.append("_____mouseButton != " + buttonNumber);
    }

    StringSourceWriter writer = new StringSourceWriter();
    writer.println("int _____mouseButton = event.getNativeEvent().getButton();");
    writer.println("if (" + condition.toString() + ") {");
    writer.indent();
    writer.println("return;");
    writer.outdent();
    writer.println("}");
    binderContext.addBeforeWrappers(writer);
  }

  @Override
  public String name() {
    return "KeyCode";
  }
}
