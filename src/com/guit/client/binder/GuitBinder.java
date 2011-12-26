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
package com.guit.client.binder;

import com.google.gwt.event.shared.EventBus;

import com.guit.client.Controller;
import com.guit.client.View;

/**
 * Guit binder.
 * 
 * @param <P> Controller
 */
public interface GuitBinder<P extends Controller> {

  /**
   * Bind the presesenter with the EventBus.
   * 
   * @param presenter Presenter
   * @param eventBus EventBus
   */
  void bind(P presenter, EventBus eventBus);

  /**
   * Create a view and bind the events. Recursive calls will return the same
   * instance until you call releaseView().
   */
  View getView();

  /**
   * Unbind all handlers and release the view.
   */
  void releaseView();

  /**
   * Remove all the registrations and call releaseView.
   */
  void destroy();
}
