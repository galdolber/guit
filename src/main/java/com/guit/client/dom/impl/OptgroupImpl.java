package com.guit.client.dom.impl;

public class OptgroupImpl extends ElementImpl implements com.guit.client.dom.Optgroup {

  public OptgroupImpl() {
    super("optgroup");
  }

  private com.google.gwt.dom.client.OptGroupElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String label() {
    return el().getLabel();
  }

  @Override
  public boolean disabled() {
    return el().isDisabled();
  }

  @Override
  public void disabled(boolean arg0) {
    el().setDisabled(arg0);
  }

  @Override
  public void setDisabled(java.lang.String arg0) {
    el().setDisabled(arg0);
  }

  @Override
  public void label(java.lang.String arg0) {
    el().setLabel(arg0);
  }
}
