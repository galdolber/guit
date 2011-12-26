package com.guit.client.binder.prefix.client;

import com.google.gwt.junit.client.GWTTestCase;

import com.guit.client.GuitEntryPoint;

import org.junit.Test;

public class PrefixINVGwtTest extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "com.guit.client.binder.prefix.PrefixINVModule";
  }

  @Test
  public void testVoid() {
    try {
      GuitEntryPoint entryPoint = new GuitEntryPoint();
      entryPoint.onModuleLoad();
    } catch (RuntimeException e) {
      e.printStackTrace();
      return;
    }

    fail("UnableToCompleteException expected as view is invalid!");
  }
}
