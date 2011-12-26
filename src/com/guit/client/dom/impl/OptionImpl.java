package com.guit.client.dom.impl;

import com.guit.client.dom.Element;

public class OptionImpl extends ElementImpl implements com.guit.client.dom.Option {

  public OptionImpl() {
    super("option");
  }

  private com.google.gwt.dom.client.OptionElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String value() {
    return el().getValue();
  }

  @Override
  public void value(java.lang.String arg0) {
    el().setValue(arg0);
  }

  @Override
  public int index() {
    return el().getIndex();
  }

  @Override
  public Element text(java.lang.String arg0) {
    el().setText(arg0);
    return this;
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
  public void label(java.lang.String arg0) {
    el().setLabel(arg0);
  }

  @Override
  public java.lang.String text() {
    return el().getText();
  }

  @Override
  public boolean isDefaultSelected() {
    return el().isDefaultSelected();
  }

  @Override
  public boolean selected() {
    return el().isSelected();
  }

  @Override
  public void defaultSelected(boolean arg0) {
    el().setDefaultSelected(arg0);
  }

  @Override
  public void selected(boolean arg0) {
    el().setSelected(arg0);
  }
}
