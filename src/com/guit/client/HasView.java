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

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface to access the view of a presenter.
 */
public interface HasView extends IsWidget {

  /**
   * @return Get a view.
   */
  View getView();

  /**
   * Asserts the view has been instantiated and the events binded. Does the same
   * that getView.
   */
  void assertView();

  /**
   * Adds the view to the panel.
   * 
   * @param panel AcceptsOneWidget panel
   */
  void setViewTo(AcceptsOneWidget panel);

  /**
   * Adds the view to the panel.
   * 
   * @param panel HasWidgets panel
   */
  void addViewTo(HasWidgets.ForIsWidget panel);

  /**
   * Release the view.
   */
  void releaseView();
}
