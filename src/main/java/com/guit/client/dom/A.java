package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.AImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.AMock;

@Implementation(AImpl.class)
@Mock(AMock.class)
public interface A extends Element {

  String accessKey();

  String href();

  String hreflang();

  String name();

  String rel();

  String target();

  String type();

  void accessKey(String accessKey);

  void href(String href);

  void hreflang(String hreflang);

  void name(String name);

  void rel(String rel);

  void target(String target);

  void type(String type);
}