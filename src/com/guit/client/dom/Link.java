package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.LinkImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.LinkMock;

@Implementation(LinkImpl.class)
@Mock(LinkMock.class)
public interface Link extends Element {

  String href();

  String hreflang();

  String media();

  String rel();

  String target();

  void href(String href);

  void hreflang(String hreflang);

  void media(String media);

  void rel(String rel);

  void target(String target);
}