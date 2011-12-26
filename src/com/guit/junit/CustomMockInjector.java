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

import com.google.inject.MembersInjector;

import java.lang.reflect.Field;

public class CustomMockInjector<I> implements MembersInjector<I> {

  private final Field field;
  private final Class<?> mockType;

  public CustomMockInjector(Field field, Class<?> mockType) {
    field.setAccessible(true);
    this.field = field;
    this.mockType = mockType;
  }

  @Override
  public void injectMembers(I i) {
    try {
      field.set(i, mockType.newInstance());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
