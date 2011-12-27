package com.guit.client.dom.impl;

import com.guit.client.dom.Element;
import com.guit.client.dom.Touch;

public class TouchImpl implements Touch {

  private com.google.gwt.dom.client.Touch t;

  public TouchImpl(com.google.gwt.dom.client.Touch t) {
    this.t = t;
  }

  @Override
  public int getClientX() {
    return t.getClientX();
  }

  @Override
  public int getClientY() {
    return t.getClientY();
  }

  @Override
  public int getIdentifier() {
    return t.getIdentifier();
  }

  @Override
  public int getPageX() {
    return t.getPageX();
  }

  @Override
  public int getPageY() {
    return t.getPageY();
  }

  @Override
  public int getRelativeX(Element target) {
    return t.getRelativeX(((ElementImpl) target).e);
  }

  @Override
  public int getRelativeY(Element target) {
    return t.getRelativeY(((ElementImpl) target).e);
  }

  @Override
  public int getScreenX() {
    return t.getScreenX();
  }

  @Override
  public int getScreenY() {
    return t.getScreenY();
  }

  @Override
  public Element getTarget() {
    com.google.gwt.dom.client.Element target = t.getTarget().cast();
    return new ElementImpl(target);
  }

}
