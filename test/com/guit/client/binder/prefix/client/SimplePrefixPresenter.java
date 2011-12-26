package com.guit.client.binder.prefix.client;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.guit.client.GuitPresenter;
import com.guit.client.ViewProperties;
import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.GuitBinder;
import com.guit.client.binder.ViewField;
import com.guit.client.binder.prefix.client.SimplePrefixPresenter.SimplePrefixPresenterBinder;

@GwtPresenter
@Singleton
@ViewProperties(template = "SimpleView.ui.xml")
public class SimplePrefixPresenter  extends GuitPresenter<SimplePrefixPresenterBinder> {
    public static interface SimplePrefixPresenterBinder extends GuitBinder<SimplePrefixPresenter> {
    }

    @ViewField
    HasText label;
    
    public static SimplePrefixPresenter instance;

    @Override
    protected void initialize() {
        RootPanel.get().add((Widget) getView());
        instance = this;
    }

}
