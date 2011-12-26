package com.guit.client.dom.impl;

public class TableImpl extends ElementImpl implements com.guit.client.dom.Table {

  public TableImpl() {
    super("table");
  }

  private com.google.gwt.dom.client.TableElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String tableWidth() {
    return el().getWidth();
  }

  @Override
  public void width(java.lang.String arg0) {
    el().setWidth(arg0);
  }

  @Override
  public int border() {
    return el().getBorder();
  }

  @Override
  public int cellPadding() {
    return el().getCellPadding();
  }

  @Override
  public int cellSpacing() {
    return el().getCellSpacing();
  }

  @Override
  public java.lang.String frame() {
    return el().getFrame();
  }

  @Override
  public java.lang.String rules() {
    return el().getRules();
  }

  @Override
  public void border(int arg0) {
    el().setBorder(arg0);
  }

  @Override
  public void cellPadding(int arg0) {
    el().setCellPadding(arg0);
  }

  @Override
  public void cellSpacing(int arg0) {
    el().setCellSpacing(arg0);
  }

  @Override
  public void frame(java.lang.String arg0) {
    el().setFrame(arg0);
  }

  @Override
  public void rules(java.lang.String arg0) {
    el().setRules(arg0);
  }
}
