package com.guit.client.dom.impl;

public class ButtonImpl extends ElementImpl implements com.guit.client.dom.Button {

  public ButtonImpl() {
    super("button");
  }

  private com.google.gwt.dom.client.ButtonElement el() {
    return e.cast();
  }

  @Override
  public void click() {
    el().click();
  }
}
