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
import com.google.gwt.editor.client.testing.MockSimpleBeanEditorDriver;
import com.google.inject.MembersInjector;
import com.google.web.bindery.requestfactory.gwt.client.testing.MockRequestFactoryEditorDriver;

import java.lang.reflect.Field;

public class DriverInjector<I> implements MembersInjector<I> {

  private final Field field;

  public DriverInjector(Field field) {
    field.setAccessible(true);
    this.field = field;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void injectMembers(I i) {
    try {
      if (field.getType().equals(SimpleBeanEditorDriver.class)) {
        field.set(i, new MockSimpleBeanEditorDriver());
      } else {
        field.set(i, new MockRequestFactoryEditorDriver());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
