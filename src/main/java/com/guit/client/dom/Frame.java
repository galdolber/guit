package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.FrameImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.FrameMock;

@Implementation(FrameImpl.class)
@Mock(FrameMock.class)
public interface Frame extends Element {

  int frameBorder();

  String longDesc();

  int marginHeight();

  int marginWidth();

  String name();

  String scrolling();

  String src();

  boolean noResize();

  void frameBorder(int frameBorder);

  void longDesc(String longDesc);

  void marginHeight(int marginHeight);

  void marginWidth(int marginWidth);

  void name(String name);

  void noResize(boolean noResize);

  void scrolling(String scrolling);

  void src(String src);
}