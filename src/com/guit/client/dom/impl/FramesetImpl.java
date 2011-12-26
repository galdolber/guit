package com.guit.client.dom.impl;

public class FramesetImpl extends ElementImpl implements com.guit.client.dom.Frameset {

  public FramesetImpl() {
    super("frameset");
  }

  private com.google.gwt.dom.client.FrameSetElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String cols() {
    return el().getCols();
  }

  @Override
  public java.lang.String rows() {
    return el().getRows();
  }

  @Override
  public void cols(java.lang.String arg0) {
    el().setCols(arg0);
  }

  @Override
  public void rows(java.lang.String arg0) {
    el().setRows(arg0);
  }
}
