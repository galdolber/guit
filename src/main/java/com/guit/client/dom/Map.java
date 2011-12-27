package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.MapImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.MapMock;

@Implementation(MapImpl.class)
@Mock(MapMock.class)
public interface Map extends Element {

  String name();

  void name(String name);
}