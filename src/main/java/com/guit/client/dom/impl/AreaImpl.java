package com.guit.client.dom.impl;

public class AreaImpl extends ElementImpl implements com.guit.client.dom.Area {

  public AreaImpl() {
    super("area");
  }

  private com.google.gwt.dom.client.AreaElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String accessKey() {
    return el().getAccessKey();
  }

  @Override
  public java.lang.String alt() {
    return el().getAlt();
  }

  @Override
  public java.lang.String coords() {
    return el().getCoords();
  }

  @Override
  public java.lang.String href() {
    return el().getHref();
  }

  @Override
  public java.lang.String shape() {
    return el().getShape();
  }

  @Override
  public java.lang.String target() {
    return el().getTarget();
  }

  @Override
  public void accessKey(java.lang.String arg0) {
    el().setAccessKey(arg0);
  }

  @Override
  public void alt(java.lang.String arg0) {
    el().setAlt(arg0);
  }

  @Override
  public void coords(java.lang.String arg0) {
    el().setCoords(arg0);
  }

  @Override
  public void href(java.lang.String arg0) {
    el().setHref(arg0);
  }

  @Override
  public void shape(java.lang.String arg0) {
    el().setShape(arg0);
  }

  @Override
  public void target(java.lang.String arg0) {
    el().setTarget(arg0);
  }
}
