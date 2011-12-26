package com.guit.client.dom.impl;

public class LabelImpl extends ElementImpl implements com.guit.client.dom.Label {
  
  public LabelImpl() {
    super("label");
  }
  
  private com.google.gwt.dom.client.LabelElement el() {
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
  
  @Override
  public java.lang.String htmlFor() {
    return el().getHtmlFor();
  }
  
  @Override
  public void htmlFor(java.lang.String arg0) {
    el().setHtmlFor(arg0);
  }
}
