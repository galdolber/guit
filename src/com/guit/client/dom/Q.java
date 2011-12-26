package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.QImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.QMock;

@Implementation(QImpl.class)
@Mock(QMock.class)
public interface Q extends Element {

  String cite();

  void cite(String cite);
}