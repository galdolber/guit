package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.FramesetImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.FramesetMock;

@Implementation(FramesetImpl.class)
@Mock(FramesetMock.class)
public interface Frameset extends Element {

  String cols();

  String rows();

  void cols(String cols);

  void rows(String rows);
}