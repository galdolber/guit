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

import com.google.gwt.event.shared.EventBus;

import com.guit.client.Controller;
import com.guit.client.View;
import com.guit.client.binder.GuitBinder;

public class GuitBinderMock<T extends Controller> implements GuitBinder<T> {

  @Override
  public void bind(T presenter, EventBus eventBus) {
  }

  @Override
  public View getView() {
    return null;
  }

  @Override
  public void releaseView() {
  }

  @Override
  public void destroy() {
  }
}
