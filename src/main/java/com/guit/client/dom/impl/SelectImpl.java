package com.guit.client.dom.impl;

public class SelectImpl extends ElementImpl implements com.guit.client.dom.Select {

  public SelectImpl() {
    super("select");
  }

  private com.google.gwt.dom.client.SelectElement el() {
    return e.cast();
  }

  @Override
  public int length() {
    return el().getLength();
  }

  @Override
  public void clear() {
    el().clear();
  }

  @Override
  public void removeItem(int arg0) {
    el().remove(arg0);
  }

  @Override
  public int size() {
    return el().getSize();
  }

  @Override
  public void size(int arg0) {
    el().setSize(arg0);
  }

  @Override
  public java.lang.String getMultiple() {
    return el().getMultiple();
  }

  @Override
  public int selectedIndex() {
    return el().getSelectedIndex();
  }

  @Override
  public boolean multiple() {
    return el().isMultiple();
  }

  @Override
  public void multiple(boolean arg0) {
    el().setMultiple(arg0);
  }

  @Override
  public void selectedIndex(int arg0) {
    el().setSelectedIndex(arg0);
  }
}
