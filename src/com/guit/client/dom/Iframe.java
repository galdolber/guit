package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.IframeImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.IframeMock;

@Implementation(IframeImpl.class)
@Mock(IframeMock.class)
public interface Iframe extends Element {

  int frameBorder();

  int marginHeight();

  int marginWidth();

  String name();

  String scrolling();

  String src();

  boolean noResize();

  void frameBorder(int frameBorder);

  void marginHeight(int marginHeight);

  void marginWidth(int marginWidth);

  void name(String name);

  void noResize(boolean noResize);

  void scrolling(String scrolling);

  void src(String src);
}