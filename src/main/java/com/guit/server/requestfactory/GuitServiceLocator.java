package com.guit.server.requestfactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class GuitServiceLocator implements ServiceLocator {

  @Inject
  Injector injector;

  @Override
  public Object getInstance(Class<?> clazz) {
    return injector.getInstance(clazz);
  }
}