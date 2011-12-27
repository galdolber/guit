package com.guit.client.dom.impl;

import com.guit.client.dom.Element;

public class ScriptImpl extends ElementImpl implements com.guit.client.dom.Script {

  public ScriptImpl() {
    super("script");
  }

  private com.google.gwt.dom.client.ScriptElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String type() {
    return el().getType();
  }

  @Override
  public Element text(java.lang.String arg0) {
    el().setText(arg0);
    return this;
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

  @Override
  public java.lang.String text() {
    return el().getText();
  }

  @Override
  public java.lang.String defer() {
    return el().getDefer();
  }

  @Override
  public void defer(java.lang.String arg0) {
    el().setDefer(arg0);
  }
}
