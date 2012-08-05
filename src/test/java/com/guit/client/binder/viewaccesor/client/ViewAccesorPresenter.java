package com.guit.client.binder.viewaccesor.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import com.guit.client.GuitPresenter;
import com.guit.client.Implementation;
import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.GuitBinder;
import com.guit.client.binder.ViewAccesor;
import com.guit.client.binder.ViewField;
import com.guit.client.binder.viewaccesor.client.ViewAccesorPresenter.ViewAccesorBinder;
import com.guit.client.dom.Div;

@GwtPresenter
@Singleton
public class ViewAccesorPresenter extends GuitPresenter<ViewAccesorBinder> {
  public static interface ViewAccesorBinder extends GuitBinder<ViewAccesorPresenter> {
  }

  @Implementation(AccesorWithImplementationImpl.class)
  public static interface AccesorWithImplementation extends ViewAccesor {
  }

  public static class AccesorWithImplementationImpl implements AccesorWithImplementation {

    @Override
    public void setTarget(Object target) {
    }
  }

  public static interface AccesorWithoutImplementation extends ViewAccesor {
  }

  public static class AccesorWithoutImplementationImpl implements AccesorWithoutImplementation {
    @Override
    public void setTarget(Object target) {
    }
  }

  @ViewField
  AccesorWithImplementation with;

  @ViewField
  AccesorWithoutImplementation without;

  @ViewField
  Div div;

  public static ViewAccesorPresenter instance;

  @Override
  protected void initialize() {
    RootPanel.get().add((Widget) getView());
    instance = this;
    div.text("Hello world");
  }

}
