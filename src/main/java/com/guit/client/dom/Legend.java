package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.LegendImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.LegendMock;

@Implementation(LegendImpl.class)
@Mock(LegendMock.class)
public interface Legend extends Element {

  String accessKey();

  void accessKey(String accessKey);
}