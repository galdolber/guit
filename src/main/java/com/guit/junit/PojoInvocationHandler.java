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

import com.google.gwt.resources.client.CssResource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Mock a pojo functionality. Geters and setters will work as expected.
 */
public class PojoInvocationHandler implements InvocationHandler {

  private final HashMap<String, Object> hash = new HashMap<String, Object>();
  private Class<?> type;

  public PojoInvocationHandler(Class<?> type) {
    this.type = type;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();

    if (CssResource.class.isAssignableFrom(type)) {
      return name;
    }

    if (name.equals("hashCode") && args == null) {
      return hashCode();
    } else if (name.equals("equals") && args.length == 1) {
      return equals(Proxy.getInvocationHandler(args[0]));
    } else if ((name.startsWith("get") || name.startsWith("is")) && args == null) {
      name = name.substring(3);
      return hash.get(name);
    } else if (name.startsWith("set") && args != null && args.length == 1) {
      name = name.substring(3);
      hash.put(name, args[0]);
      return null;
    } else {
      return null;
    }
  }
}
