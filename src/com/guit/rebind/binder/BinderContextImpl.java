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
package com.guit.rebind.binder;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;

import java.util.ArrayList;
import java.util.Stack;

public class BinderContextImpl implements BinderContext {

  private final SourceWriter beforeWrappers = new StringSourceWriter();
  private final SourceWriter beforeHandler = new StringSourceWriter();
  private final SourceWriter afterHandler = new StringSourceWriter();
  private final SourceWriter afterWrappers = new StringSourceWriter();
  private final ArrayList<SourceWriter> wrappersBefore = new ArrayList<SourceWriter>();
  private final Stack<SourceWriter> wrappersAfter = new Stack<SourceWriter>();
  private final StringBuilder logger = new StringBuilder();
  private final String viewTypeName;

  private final JMethod method;

  private final JClassType eventType;
  private final JClassType presenterType;

  private final String[] parameterGetters;

  public BinderContextImpl(JMethod method, JClassType eventType, JClassType presenterType,
      String viewTypeName, String[] parameterGetters) {
    this.method = method;
    this.eventType = eventType;
    this.presenterType = presenterType;
    this.viewTypeName = viewTypeName;
    this.parameterGetters = parameterGetters;
  }

  @Override
  public void addAfterHandler(StringSourceWriter writer) {
    afterHandler.println(writer.toString());
  }

  @Override
  public void addAfterWrappers(StringSourceWriter writer) {
    afterWrappers.println(writer.toString());
  }

  @Override
  public void addBeforeHandler(StringSourceWriter writer) {
    beforeHandler.println(writer.toString());
  }

  @Override
  public void addBeforeWrappers(StringSourceWriter writer) {
    beforeWrappers.println(writer.toString());
  }

  @Override
  public void addWrapper(StringSourceWriter before, StringSourceWriter after) {
    wrappersBefore.add(before);
    wrappersAfter.push(after);
  }

  @Override
  public String build(StringSourceWriter writer) {
    StringSourceWriter build = new StringSourceWriter();

    build.print(beforeWrappers.toString());
    for (SourceWriter w : wrappersBefore) {
      build.print(w.toString());
    }
    build.print(beforeHandler.toString());
    build.print(writer.toString());
    build.print(afterHandler.toString());
    for (SourceWriter w : wrappersAfter) {
      build.print(w.toString());
    }
    build.print(afterWrappers.toString());

    return build.toString();
  }

  @Override
  public JClassType getEventType() {
    return eventType;
  }

  @Override
  public String getLog() {
    // Print parameters values
    JParameter[] parameters = method.getParameters();
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int n = 0; n < parameters.length; n++) {
      if (n > 0) {
        sb.append(", ");
      }
      sb.append(parameters[n].getName() + "=" + "\" + " + parameterGetters[n] + " + \"");
    }
    sb.append("]");

    String logString = logger.toString();
    return presenterType.getSimpleSourceName() + "." + method.getName() + sb.toString()
        + (logString.isEmpty() ? "" : ("\\n  Plugins=[" + logString + "]"))
        + "\\n  DebugString=[\" + event.toDebugString() + \"]" + "\\n  Package=["
        + presenterType.getPackage().getName() + "]";
  }

  @Override
  public JMethod getMethod() {
    return method;
  }

  @Override
  public String[] getParameterGetters() {
    return parameterGetters;
  }

  @Override
  public JClassType getPresenterType() {
    return presenterType;
  }

  @Override
  public String getViewTypeName() {
    return viewTypeName;
  }

  @Override
  public void log(String log) {
    if (logger.length() > 0) {
      logger.append(", ");
    }
    logger.append(log);
  }
}
