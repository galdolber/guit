package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.BlockquoteImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.BlockquoteMock;

@Implementation(BlockquoteImpl.class)
@Mock(BlockquoteMock.class)
public interface Blockquote extends Element {

  String cite();

  void cite(String cite);
}