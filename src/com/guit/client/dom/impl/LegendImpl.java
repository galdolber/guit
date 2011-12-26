package com.guit.client.dom.impl;

public class LegendImpl extends ElementImpl implements com.guit.client.dom.Legend {
  
  public LegendImpl() {
    super("legend");
  }
  
  private com.google.gwt.dom.client.LegendElement el() {
    return e.cast();
  }
  
  @Override
  public java.lang.String accessKey() {
    return el().getAccessKey();
  }
  
  @Override
  public void accessKey(java.lang.String arg0) {
    el().setAccessKey(arg0);
  }
}
