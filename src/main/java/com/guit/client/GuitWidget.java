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
package com.guit.client;

import com.guit.client.binder.GuitBinder;

/**
 * A guit widget like uibinder but using guit sintax to bind events. It have no
 * access to eventbus, placemanager or commandservice.
 * 
 * @param <B> Binder
 */
@SuppressWarnings("rawtypes")
public class GuitWidget<B extends GuitBinder> extends GuitPresenterBase<B> {

  @SuppressWarnings("unchecked")
  public GuitWidget(B binder) {
    this.binder = binder;
    binder.bind(this, null);
    initialize();
  }
}
