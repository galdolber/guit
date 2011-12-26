package com.guit.client.dom.impl;

public class IframeImpl extends ElementImpl implements com.guit.client.dom.Iframe {

  public IframeImpl() {
    super("iframe");
  }

  private com.google.gwt.dom.client.IFrameElement el() {
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
  public int frameBorder() {
    return el().getFrameBorder();
  }

  @Override
  public int marginHeight() {
    return el().getMarginHeight();
  }

  @Override
  public int marginWidth() {
    return el().getMarginWidth();
  }

  @Override
  public java.lang.String scrolling() {
    return el().getScrolling();
  }

  @Override
  public java.lang.String src() {
    return el().getSrc();
  }

  @Override
  public boolean noResize() {
    return el().isNoResize();
  }

  @Override
  public void frameBorder(int arg0) {
    el().setFrameBorder(arg0);
  }

  @Override
  public void marginHeight(int arg0) {
    el().setMarginHeight(arg0);
  }

  @Override
  public void marginWidth(int arg0) {
    el().setMarginWidth(arg0);
  }

  @Override
  public void noResize(boolean arg0) {
    el().setNoResize(arg0);
  }

  @Override
  public void scrolling(java.lang.String arg0) {
    el().setScrolling(arg0);
  }

  @Override
  public void src(java.lang.String arg0) {
    el().setSrc(arg0);
  }
}
