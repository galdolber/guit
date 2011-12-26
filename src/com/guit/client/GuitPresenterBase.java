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
import com.google.gwt.user.client.ui.Widget;

import com.guit.client.binder.GuitBinder;

/**
 * Base presenter.
 * 
 * @param <B> Binder class.
 */
@SuppressWarnings("rawtypes")
public abstract class GuitPresenterBase<B extends GuitBinder> implements Presenter {

  protected B binder;

  @Override
  public View getView() {
    assert binder != null : "The binder is null in the contructor, override initialize()";
    return binder.getView();
  }

  /**
   * Get called after the binder, eventbus, commandservice and placemanager get
   * injected.
   */
  protected void initialize() {
  }

  /**
   * Release the view and unbind all view events.
   */
  @Override
  public void releaseView() {
    assert binder != null : "The binder is null in the contructor, override initialize()";
    binder.releaseView();
  }

  @Override
  public void destroy() {
    assert binder != null : "The presenter is already destroyed";
    binder.destroy();
    binder = null;
  }

  @Override
  public Widget asWidget() {
    return getView().asWidget();
  }

  @Override
  public void setViewTo(AcceptsOneWidget panel) {
    assert panel != null : "The panel cannot be null";
    panel.setWidget(getView());
  }

  @Override
  public void addViewTo(HasWidgets.ForIsWidget panel) {
    assert panel != null : "The panel cannot be null";
    panel.add(getView());
  }

  @Override
  public void assertView() {
    getView();
  }
}
