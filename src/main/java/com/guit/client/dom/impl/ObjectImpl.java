package com.guit.client.dom.impl;

public class ObjectImpl extends ElementImpl implements com.guit.client.dom.Object {

  public ObjectImpl() {
    super("object");
  }

  private com.google.gwt.dom.client.ObjectElement el() {
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

  @Override
  public java.lang.String type() {
    return el().getType();
  }

  @Override
  public java.lang.String data() {
    return el().getData();
  }

  @Override
  public java.lang.String objectHeight() {
    return el().getHeight();
  }

  @Override
  public java.lang.String objectWidth() {
    return el().getWidth();
  }

  @Override
  public void height(java.lang.String arg0) {
    el().setHeight(arg0);
  }

  @Override
  public void useMap(boolean arg0) {
    el().setUseMap(arg0);
  }

  @Override
  public void width(java.lang.String arg0) {
    el().setWidth(arg0);
  }

  @Override
  public boolean useMap() {
    return el().useMap();
  }

  @Override
  public java.lang.String code() {
    return el().getCode();
  }

  @Override
  public void code(java.lang.String arg0) {
    el().setCode(arg0);
  }

  @Override
  public void data(java.lang.String arg0) {
    el().setData(arg0);
  }

  @Override
  public void type(java.lang.String arg0) {
    el().setType(arg0);
  }
}
