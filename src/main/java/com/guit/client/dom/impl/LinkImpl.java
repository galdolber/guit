package com.guit.client.dom.impl;

public class LinkImpl extends ElementImpl implements com.guit.client.dom.Link {

  public LinkImpl() {
    super("link");
  }

  private com.google.gwt.dom.client.LinkElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String href() {
    return el().getHref();
  }

  @Override
  public java.lang.String target() {
    return el().getTarget();
  }

  @Override
  public void href(java.lang.String arg0) {
    el().setHref(arg0);
  }

  @Override
  public void target(java.lang.String arg0) {
    el().setTarget(arg0);
  }

  @Override
  public java.lang.String hreflang() {
    return el().getHreflang();
  }

  @Override
  public java.lang.String media() {
    return el().getMedia();
  }

  @Override
  public java.lang.String rel() {
    return el().getRel();
  }

  @Override
  public void hreflang(java.lang.String arg0) {
    el().setHreflang(arg0);
  }

  @Override
  public void media(java.lang.String arg0) {
    el().setMedia(arg0);
  }

  @Override
  public void rel(java.lang.String arg0) {
    el().setRel(arg0);
  }
}
