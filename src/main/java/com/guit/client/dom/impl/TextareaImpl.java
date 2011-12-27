package com.guit.client.dom.impl;

import com.guit.client.dom.Element;

public class TextareaImpl extends ElementImpl implements com.guit.client.dom.Textarea {

  public TextareaImpl() {
    super("textarea");
  }

  private com.google.gwt.dom.client.TextAreaElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String value() {
    return el().getValue();
  }

  @Override
  public void readOnly(boolean arg0) {
    el().setReadOnly(arg0);
  }

  @Override
  public java.lang.String defaultValue() {
    return el().getDefaultValue();
  }

  @Override
  public boolean readOnly() {
    return el().isReadOnly();
  }

  @Override
  public Element value(java.lang.String arg0) {
    el().setValue(arg0);
    return this;
  }

  @Override
  public int cols() {
    return el().getCols();
  }

  @Override
  public int rows() {
    return el().getRows();
  }

  @Override
  public void cols(int arg0) {
    el().setCols(arg0);
  }

  @Override
  public void rows(int arg0) {
    el().setRows(arg0);
  }

  @Override
  public void select() {
    el().select();
  }

  @Override
  public void defaultValue(java.lang.String arg0) {
    el().setDefaultValue(arg0);
  }
}
