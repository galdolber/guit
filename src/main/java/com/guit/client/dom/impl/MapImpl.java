package com.guit.client.dom.impl;

public class MapImpl extends ElementImpl implements com.guit.client.dom.Map {

  public MapImpl() {
    super("map");
  }

  private com.google.gwt.dom.client.MapElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String name() {
    return el().getName();
  }

  @Override
  public void name(java.lang.String arg0) {
    el().setName(arg0);
  }
}
