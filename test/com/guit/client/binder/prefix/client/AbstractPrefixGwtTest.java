package com.guit.client.binder.prefix.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.guit.client.GuitEntryPoint;

public abstract class AbstractPrefixGwtTest extends GWTTestCase {

    SimplePrefixPresenter presenter;

    @Override
    protected void gwtSetUp() throws Exception {
        GuitEntryPoint entryPoint = new GuitEntryPoint();
        entryPoint.onModuleLoad();
        presenter = SimplePrefixPresenter.instance;
    }
}
