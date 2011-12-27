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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.rebind.StringSourceWriter;

import com.guit.client.binder.contributor.HasAttribute;
import com.guit.rebind.binder.BinderContext;
import com.guit.rebind.binder.BinderContributor;

public class HasAttributeContributor implements BinderContributor {

  private static final String domEventsPackage = "com.google.gwt.event.dom.client";
  private static final String elementName = Element.class.getCanonicalName();

  @Override
  public void contribute(BinderContext binderContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    JMethod m = binderContext.getMethod();
    JClassType eventType = binderContext.getEventType();
    JClassType presenterType = binderContext.getPresenterType();
    if (!eventType.getPackage().getName().equals(domEventsPackage)) {
      logger.log(Type.ERROR, String.format(
          "@HasAttribute annotation is only valid for dom events. Found: %s.%s", presenterType
              .getQualifiedSourceName(), m.getName()));
      throw new UnableToCompleteException();
    }

    String[] attributes = binderContext.getMethod().getAnnotation(HasAttribute.class).value();

    StringSourceWriter writer = new StringSourceWriter();
    writer.println(elementName + " _has_attribute_element = (" + elementName
        + ")event.getNativeEvent().getEventTarget().cast();");
    writer.print("if (");
    for (int n = 0; n < attributes.length; n++) {
      if (n > 0) {
        writer.print(" || ");
      }
      writer.print("!_has_attribute_element.hasAttribute(\"" + attributes[n] + "\")");
    }
    writer.print(") {return;}");
    binderContext.addBeforeWrappers(writer);
  }

  @Override
  public String name() {
    return "HasAttribute";
  }
}
