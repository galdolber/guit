package com.guit.client.dom.impl;

public class AImpl extends ElementImpl implements com.guit.client.dom.A {

  public AImpl() {
    super("a");
  }

  private com.google.gwt.dom.client.AnchorElement el() {
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
  public void type(java.lang.String arg0) {
    el().setType(arg0);
  }

  @Override
  public void target(java.lang.String arg0) {
    el().setTarget(arg0);
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
  public java.lang.String accessKey() {
    return el().getAccessKey();
  }

  @Override
  public void href(java.lang.String arg0) {
    el().setHref(arg0);
  }

  @Override
  public java.lang.String href() {
    return el().getHref();
  }

  @Override
  public void hreflang(java.lang.String arg0) {
    el().setHreflang(arg0);
  }

  @Override
  public java.lang.String hreflang() {
    return el().getHreflang();
  }

  @Override
  public java.lang.String rel() {
    return el().getRel();
  }

  @Override
  public void rel(java.lang.String arg0) {
    el().setRel(arg0);
  }
}
