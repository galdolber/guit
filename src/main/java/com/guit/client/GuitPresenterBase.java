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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
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
    assert binder != null : "The binder is null in the contructor, override initialize(). Found: " + getClass().getName();
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

  // --------- //
  
  public void setWidth(String width) {
    getView().asWidget().setWidth(width);
  }

  public void setHeight(String height) {
    getView().asWidget().setHeight(height);
  }

  public void setVisible(boolean visible) {
    getView().asWidget().setVisible(visible);
  }

  public void setTitle(String title) {
    getView().asWidget().setTitle(title);
  }

  public void setStyleName(String style) {
    getView().asWidget().setStyleName(style);
  }

  public void setStylePrimaryName(String style) {
    getView().asWidget().setStylePrimaryName(style);
  }

  public boolean isVisible() {
    return getView().asWidget().isVisible();
  }

  public void addStyleDependentName(String styleSuffix) {
    getView().asWidget().addStyleDependentName(styleSuffix);
  }

  public void addStyleName(String style) {
    getView().asWidget().addStyleName(style);
  }

  public final void ensureDebugId(String id) {
    getView().asWidget().ensureDebugId(id);
  }

  public int getAbsoluteLeft() {
    return getView().asWidget().getAbsoluteLeft();
  }

  public int getAbsoluteTop() {
    return getView().asWidget().getAbsoluteTop();
  }

  public int getOffsetHeight() {
    return getView().asWidget().getOffsetHeight();
  }

  public int getOffsetWidth() {
    return getView().asWidget().getOffsetWidth();
  }

  public String getStyleName() {
    return getView().asWidget().getStyleName();
  }

  public String getStylePrimaryName() {
    return getView().asWidget().getStylePrimaryName();
  }

  public String getTitle() {
    return getView().asWidget().getTitle();
  }

  public void removeStyleDependentName(String styleSuffix) {
    getView().asWidget().removeStyleDependentName(styleSuffix);
  }

  public void removeStyleName(String style) {
    getView().asWidget().removeStyleName(style);
  }

  public void setPixelSize(int width, int height) {
    getView().asWidget().setPixelSize(width, height);
  }

  public void setSize(String width, String height) {
    getView().asWidget().setSize(width, height);
  }

  public void setStyleDependentName(String styleSuffix, boolean add) {
    getView().asWidget().setStyleDependentName(styleSuffix, add);
  }

  public void setStyleName(String style, boolean add) {
    getView().asWidget().setStyleName(style, add);
  }

  public <H extends EventHandler> HandlerRegistration addHandler(H handler, GwtEvent.Type<H> type) {
    return getView().asWidget().addHandler(handler, type);
  }

  public void fireEvent(GwtEvent<?> event) {
    getView().asWidget().fireEvent(event);
  }

}
