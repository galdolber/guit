package com.guit.server.requestfactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@Singleton
public class GuitRequestFactoryServlet extends RequestFactoryServlet {

  @Inject
  public GuitRequestFactoryServlet(GuitServiceLayerDecorator guiceServiceLayerDecorator) {
    super(new DefaultExceptionHandler(), guiceServiceLayerDecorator);
  }
}
