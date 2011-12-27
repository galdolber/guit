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
import com.google.gwt.user.rebind.StringSourceWriter;

public interface BinderContext {

  void addAfterHandler(StringSourceWriter writer);

  void addAfterWrappers(StringSourceWriter writer);

  void addBeforeHandler(StringSourceWriter writer);

  void addBeforeWrappers(StringSourceWriter writer);

  void addWrapper(StringSourceWriter before, StringSourceWriter after);

  String build(StringSourceWriter writer);

  JClassType getEventType();

  String getLog();

  JMethod getMethod();

  String[] getParameterGetters();

  JClassType getPresenterType();

  String getViewTypeName();

  void log(String log);
}
