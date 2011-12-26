package com.guit.client.dom.impl;

public class QImpl extends ElementImpl implements com.guit.client.dom.Q {

  public QImpl() {
    super("q");
  }

  private com.google.gwt.dom.client.QuoteElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String cite() {
    return el().getCite();
  }

  @Override
  public void cite(java.lang.String arg0) {
    el().setCite(arg0);
  }
}
