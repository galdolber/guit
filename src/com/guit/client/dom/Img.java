package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.ImgImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ImgMock;

@Implementation(ImgImpl.class)
@Mock(ImgMock.class)
public interface Img extends Element {

  String alt();

  int imageHeight();

  String src();

  int imageWidth();

  boolean map();

  void alt(String alt);

  void height(int height);

  void map(boolean isMap);

  void src(String src);

  void useMap(boolean useMap);

  void width(int width);

  boolean useMap();
}