package com.guit.client.dom.impl;

public class ParamImpl extends ElementImpl implements com.guit.client.dom.Param {

  public ParamImpl() {
    super("param");
  }

  private com.google.gwt.dom.client.ParamElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String name() {
    return el().getName();
  }

  @Override
  public java.lang.String value() {
    return el().getValue();
  }

  @Override
  public void name(java.lang.String arg0) {
    el().setName(arg0);
  }

  @Override
  public void value(java.lang.String arg0) {
    el().setValue(arg0);
  }
}
