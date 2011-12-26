package com.guit.junit.dom;

import com.guit.client.dom.Q;

public class QMock extends ElementMock implements Q {

  public QMock() {
    super("q");
  }

  @Override
  public String cite() {
    return attr("cite");
  }

  @Override
  public void cite(String cite) {
    attr("cite", cite);
  }
}
