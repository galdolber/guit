package com.guit.client.binder;

import com.google.gwt.dom.client.Element;

public class IElementImpl implements IElement {

  private Element target;

  @Override
  public void setTarget(Object target) {
    this.target = (Element) target;
  }

  @Override
  public Element getElement() {
    return target;
  }
}
