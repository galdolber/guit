package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.AreaImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.AreaMock;

@Implementation(AreaImpl.class)
@Mock(AreaMock.class)
public interface Area extends Element {

  String accessKey();

  String alt();

  String coords();

  String href();

  String shape();

  String target();

  void accessKey(String accessKey);

  void alt(String alt);

  void coords(String coords);

  void href(String href);

  void shape(String shape);

  void target(String target);
}