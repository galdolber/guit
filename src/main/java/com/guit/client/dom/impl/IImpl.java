package com.guit.client.dom.impl;

import com.guit.client.dom.Element;

public class IImpl extends ElementImpl implements com.guit.client.dom.I {

  public IImpl() {
    super("i");
  }

  @Override
  public Element setClassName(String className) {
    e.setClassName(className);
    return this;
  }
}