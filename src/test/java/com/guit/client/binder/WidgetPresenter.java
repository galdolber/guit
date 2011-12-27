package com.guit.client.binder;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import com.guit.client.GuitPresenter;
import com.guit.client.ViewProperties;
import com.guit.client.ViewType;
import com.guit.client.binder.WidgetPresenter.WidgetPresenterBinder;

@ViewProperties(type = ViewType.WIDGET)
@Singleton
public class WidgetPresenter extends GuitPresenter<WidgetPresenterBinder> {

  public interface WidgetPresenterBinder extends GuitBinder<WidgetPresenter> {
  }

  boolean divclicked;

  @ViewField
  IElement div;

  @ViewField
  IElement div1;

  @ViewField
  IElement span;

  boolean div1clicked;

  boolean spanclicked;

  public static WidgetPresenter instance;

  @Override
  protected void initialize() {
    // Should not do this in real code
    RootPanel.get().add((Widget) getView());
    instance = this;
  }

  @ViewHandler
  void div$click() {
    divclicked = true;
  }

  @ViewHandler
  void div1$click() {
    div1clicked = true;
  }

  @ViewHandler
  void span$click() {
    spanclicked = true;
  }
}
