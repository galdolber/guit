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

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

import com.guit.client.binder.ViewField;
import com.guit.client.dom.Element;
import com.guit.client.junit.Mock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class GuitTestListener implements TypeListener {

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
    // Collect all types
    Class<? super I> clazz = typeLiteral.getRawType();
    ArrayList<Class<?>> types = new ArrayList<Class<?>>();
    while (clazz != null && !clazz.equals(Object.class)) {
      types.add(clazz);
      clazz = clazz.getSuperclass();
    }
    Collections.reverse(types);

    HashSet<Method> methods = new HashSet<Method>();

    for (Class<?> rawType : types) {
      methods.addAll(Arrays.asList(rawType.getDeclaredMethods()));

      for (Field field : rawType.getDeclaredFields()) {
        Class<?> type = field.getType();
        if (field.isAnnotationPresent(ViewField.class)) {
          if (Element.class.isAssignableFrom(type)) {
            continue;
          }
          if (type.isAnnotationPresent(Mock.class)) {
            typeEncounter.register(new MockInjector<I>(field));
          } else {
            Class<?> mockType =
                getType(type.getPackage().getName() + ".Mock" + type.getSimpleName());
            if (mockType != null) {
              typeEncounter.register(new CustomMockInjector<I>(field, mockType));
            } else {
              if (HasWidgets.class.isAssignableFrom(type)) {
                typeEncounter.register(new CustomMockInjector<I>(field, MockHasWidgets.class));
              } else if (InsertPanel.class.isAssignableFrom(type)) {
                typeEncounter.register(new CustomMockInjector<I>(field, MockInsertPanel.class));
              } else if (Constants.class.isAssignableFrom(type)) {
                typeEncounter.register(new ConstantInjector<I>(field));
              } else if (field.getAnnotation(Inject.class) == null
                  && field.getAnnotation(javax.inject.Inject.class) == null) {
                typeEncounter.register(new ViewFieldInjector<I>(field));
              }
            }
          }
        } else if (field.getName().equals("driver")) {
          if (type.equals(SimpleBeanEditorDriver.class)
              || type.equals(RequestFactoryEditorDriver.class)) {
            typeEncounter.register(new DriverInjector<I>(field));
          }
        }
      }
    }
  }

  private Class<?> getType(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
