package com.guit.client.dom;

import com.google.gwt.event.shared.HandlerRegistration;

public interface GuitDom {

  HandlerRegistration addNativePreviewHandler(EventHandler handler);

  Body getDocumentBody();

  Head getDocumentHead();

  Element elementById(String id);

  Element element(String tag);

  B b();
  
  B b(Element e);
  
  Head head();

  H2 h2();

  Label label();

  Blockquote blockquote();

  Option option();

  Legend legend();

  H4 h4();

  Select select();

  Object object();

  Area area();

  Iframe iframe();

  Optgroup optgroup();

  Button button();

  Img img();

  Fieldset fieldset();

  Dl dl();

  Q q();

  Ol ol();

  H1 h1();

  Body body();

  Hr hr();

  H5 h5();

  Ul ul();

  Frameset frameset();

  Table table();

  Caption caption();

  Source source();

  P p();

  Map map();

  Textarea textarea();

  Frame frame();

  Form form();

  Param param();

  A a();

  Link link();

  Span span();

  H6 h6();

  Title title();

  H3 h3();

  Div div();

  Input input();

  Script script();

  Br br();

  Style style();

  Pre pre();

  Li li();

  Element element(Element e);

  Head head(Element e);

  H2 h2(Element e);

  Label label(Element e);

  Blockquote blockquote(Element e);

  Option option(Element e);

  Legend legend(Element e);

  H4 h4(Element e);

  Select select(Element e);

  Object object(Element e);

  Area area(Element e);

  Iframe iframe(Element e);

  Optgroup optgroup(Element e);

  Button button(Element e);

  Img img(Element e);

  Fieldset fieldset(Element e);

  Dl dl(Element e);

  Q q(Element e);

  Ol ol(Element e);

  H1 h1(Element e);

  Body body(Element e);

  Hr hr(Element e);

  H5 h5(Element e);

  Ul ul(Element e);

  Frameset frameset(Element e);

  Table table(Element e);

  Caption caption(Element e);

  Source source(Element e);

  P p(Element e);

  Map map(Element e);

  Textarea textarea(Element e);

  Frame frame(Element e);

  Form form(Element e);

  Param param(Element e);

  A a(Element e);

  Link link(Element e);

  Span span(Element e);

  H6 h6(Element e);

  Title title(Element e);

  H3 h3(Element e);

  Div div(Element e);

  Input input(Element e);

  Script script(Element e);

  Br br(Element e);

  Style style(Element e);

  Pre pre(Element e);

  Li li(Element e);

}