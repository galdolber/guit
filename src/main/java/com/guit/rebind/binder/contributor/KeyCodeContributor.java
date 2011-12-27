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
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.rebind.StringSourceWriter;

import com.guit.client.binder.contributor.KeyCode;
import com.guit.rebind.binder.BinderContext;
import com.guit.rebind.binder.BinderContributor;

public class KeyCodeContributor implements BinderContributor {

  @Override
  public void contribute(BinderContext binderContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    JClassType eventType = binderContext.getEventType();
    String eventName = eventType.getQualifiedSourceName();
    if (!eventName.equals(KeyPressEvent.class.getCanonicalName())
        && !eventName.equals(KeyDownEvent.class.getCanonicalName())
        && !eventName.equals(KeyUpEvent.class.getCanonicalName())) {
      logger.log(Type.ERROR, String.format(
          "The binder annotation %s is only valid in %s, %s or %s events. Found: %s.%s",
          KeyCode.class.getCanonicalName(), KeyPressEvent.class.getCanonicalName(),
          KeyDownEvent.class.getCanonicalName(), KeyUpEvent.class.getCanonicalName(), binderContext
              .getPresenterType().getQualifiedSourceName(), binderContext.getMethod().getName()));
      throw new UnableToCompleteException();
    }

    int[] keycodes = binderContext.getMethod().getAnnotation(KeyCode.class).value();
    StringBuilder condition = new StringBuilder();
    for (int keyCode : keycodes) {
      if (condition.length() > 0) {
        condition.append(" && ");
      }
      condition.append("_____keyCode != " + keyCode);
    }

    StringSourceWriter writer = new StringSourceWriter();
    writer.println("int _____keyCode = event.getNativeEvent().getKeyCode();");
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
