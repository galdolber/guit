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

import com.google.gwt.i18n.client.Constants.DefaultBooleanValue;
import com.google.gwt.i18n.client.Constants.DefaultDoubleValue;
import com.google.gwt.i18n.client.Constants.DefaultFloatValue;
import com.google.gwt.i18n.client.Constants.DefaultIntValue;
import com.google.gwt.i18n.client.Constants.DefaultStringArrayValue;
import com.google.gwt.i18n.client.Constants.DefaultStringMapValue;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Mock a constant functionality. <br/>
 * TODO Property files support?
 */
public class ConstantInvocationHandler implements InvocationHandler {

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.getName().equals("equals") && args.length == 1) {
      return equals(Proxy.getInvocationHandler(args[0]));
    } else if (method.isAnnotationPresent(DefaultStringValue.class)) {
      return method.getAnnotation(DefaultStringValue.class).value();
    } else if (method.isAnnotationPresent(DefaultBooleanValue.class)) {
      return method.getAnnotation(DefaultBooleanValue.class).value();
    } else if (method.isAnnotationPresent(DefaultFloatValue.class)) {
      return method.getAnnotation(DefaultFloatValue.class).value();
    } else if (method.isAnnotationPresent(DefaultIntValue.class)) {
      return method.getAnnotation(DefaultIntValue.class).value();
    } else if (method.isAnnotationPresent(DefaultDoubleValue.class)) {
      return method.getAnnotation(DefaultDoubleValue.class).value();
    } else if (method.isAnnotationPresent(DefaultStringArrayValue.class)) {
      return method.getAnnotation(DefaultStringArrayValue.class).value();
    } else if (method.isAnnotationPresent(DefaultStringMapValue.class)) {
      return method.getAnnotation(DefaultStringMapValue.class).value();
    }
    return null;
  }
}
