package com.guit.junit.dom;

import com.guit.client.dom.Blockquote;

public class BlockquoteMock extends ElementMock implements Blockquote {

  public BlockquoteMock() {
    super("blockquote");
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
