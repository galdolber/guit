package com.guit.client.dom.impl;

public class SourceImpl extends ElementImpl implements com.guit.client.dom.Source {
  
  public SourceImpl() {
    super("source");
  }
  
  private com.google.gwt.dom.client.SourceElement el() {
    return e.cast();
  }
  
  @Override
  public java.lang.String type() {
    return el().getType();
  }
  
  @Override
  public java.lang.String src() {
    return el().getSrc();
  }
  
  @Override
  public void src(java.lang.String arg0) {
    el().setSrc(arg0);
  }
  
  @Override
  public void type(java.lang.String arg0) {
    el().setType(arg0);
  }
}
