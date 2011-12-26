package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.StyleImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.StyleMock;

@Implementation(StyleImpl.class)
@Mock(StyleMock.class)
public interface Style extends Element {

  String cssText();

  String media();

  void cssText(String cssText);

  void media(String media);
}