package com.guit.client.dom.impl;

import com.guit.client.dom.Element;

public class InputImpl extends ElementImpl implements com.guit.client.dom.Input {

  public InputImpl() {
    super("input");
  }

  private com.google.gwt.dom.client.InputElement el() {
    return e.cast();
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
  public int size() {
    return el().getSize();
  }

  @Override
  public boolean readOnly() {
    return el().isReadOnly();
  }

  @Override
  public void size(int arg0) {
    el().setSize(arg0);
  }

  @Override
  public java.lang.String alt() {
    return el().getAlt();
  }

  @Override
  public void alt(java.lang.String arg0) {
    el().setAlt(arg0);
  }

  @Override
  public void click() {
    el().click();
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
  public void useMap(boolean arg0) {
    el().setUseMap(arg0);
  }

  @Override
  public boolean useMap() {
    return el().useMap();
  }

  @Override
  public java.lang.String accept() {
    return el().getAccept();
  }

  @Override
  public int maxLength() {
    return el().getMaxLength();
  }

  @Override
  public boolean checked() {
    return el().isChecked();
  }

  @Override
  public boolean defaultChecked() {
    return el().isDefaultChecked();
  }

  @Override
  public void select() {
    el().select();
  }

  @Override
  public void accept(java.lang.String arg0) {
    el().setAccept(arg0);
  }

  @Override
  public void checked(boolean arg0) {
    el().setChecked(arg0);
  }

  @Override
  public void defaultChecked(boolean arg0) {
    el().setDefaultChecked(arg0);
  }

  @Override
  public void defaultValue(java.lang.String arg0) {
    el().setDefaultValue(arg0);
  }

  @Override
  public void maxLength(int arg0) {
    el().setMaxLength(arg0);
  }

  @Override
  public String text() {
    return el().getValue();
  }

  @Override
  public Element text(String html) {
    el().setValue(html);
    return this;
  }

  @Override
  public String value() {
    return text();
  }

  @Override
  public Element value(String value) {
    return text(value);
  }
}
