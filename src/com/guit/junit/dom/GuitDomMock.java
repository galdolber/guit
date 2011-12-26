package com.guit.junit.dom;

import com.google.gwt.event.shared.HandlerRegistration;

import com.guit.client.dom.A;
import com.guit.client.dom.Area;
import com.guit.client.dom.Blockquote;
import com.guit.client.dom.Body;
import com.guit.client.dom.Br;
import com.guit.client.dom.Button;
import com.guit.client.dom.Caption;
import com.guit.client.dom.Div;
import com.guit.client.dom.Dl;
import com.guit.client.dom.Element;
import com.guit.client.dom.EventHandler;
import com.guit.client.dom.Fieldset;
import com.guit.client.dom.Form;
import com.guit.client.dom.Frame;
import com.guit.client.dom.Frameset;
import com.guit.client.dom.GuitDom;
import com.guit.client.dom.H1;
import com.guit.client.dom.H2;
import com.guit.client.dom.H3;
import com.guit.client.dom.H4;
import com.guit.client.dom.H5;
import com.guit.client.dom.H6;
import com.guit.client.dom.Head;
import com.guit.client.dom.Hr;
import com.guit.client.dom.Iframe;
import com.guit.client.dom.Img;
import com.guit.client.dom.Input;
import com.guit.client.dom.Label;
import com.guit.client.dom.Legend;
import com.guit.client.dom.Li;
import com.guit.client.dom.Link;
import com.guit.client.dom.Map;
import com.guit.client.dom.Object;
import com.guit.client.dom.Ol;
import com.guit.client.dom.Optgroup;
import com.guit.client.dom.Option;
import com.guit.client.dom.P;
import com.guit.client.dom.Param;
import com.guit.client.dom.Pre;
import com.guit.client.dom.Q;
import com.guit.client.dom.Script;
import com.guit.client.dom.Select;
import com.guit.client.dom.Source;
import com.guit.client.dom.Span;
import com.guit.client.dom.Style;
import com.guit.client.dom.Table;
import com.guit.client.dom.Textarea;
import com.guit.client.dom.Title;
import com.guit.client.dom.Ul;

public class GuitDomMock implements GuitDom {

  @Override
  public Element element(String tag) {
    return new ElementMock(tag);
  }

  public Head head() {
    return new HeadMock();
  }

  public H2 h2() {
    return new H2Mock();
  }

  public Label label() {
    return new LabelMock();
  }

  public Blockquote blockquote() {
    return new BlockquoteMock();
  }

  public Option option() {
    return new OptionMock();
  }

  public Legend legend() {
    return new LegendMock();
  }

  public H4 h4() {
    return new H4Mock();
  }

  public Select select() {
    return new SelectMock();
  }

  public com.guit.client.dom.Object object() {
    return new ObjectMock();
  }

  public Area area() {
    return new AreaMock();
  }

  public Iframe iframe() {
    return new IframeMock();
  }

  public Optgroup optgroup() {
    return new OptgroupMock();
  }

  public Button button() {
    return new ButtonMock();
  }

  public Img img() {
    return new ImgMock();
  }

  public Fieldset fieldset() {
    return new FieldsetMock();
  }

  public Dl dl() {
    return new DlMock();
  }

  public Q q() {
    return new QMock();
  }

  public Ol ol() {
    return new OlMock();
  }

  public H1 h1() {
    return new H1Mock();
  }

  public Body body() {
    return new BodyMock();
  }

  public Hr hr() {
    return new HrMock();
  }

  public H5 h5() {
    return new H5Mock();
  }

  public Ul ul() {
    return new UlMock();
  }

  public Frameset frameset() {
    return new FramesetMock();
  }

  public Table table() {
    return new TableMock();
  }

  public Caption caption() {
    return new CaptionMock();
  }

  public Source source() {
    return new SourceMock();
  }

  public P p() {
    return new PMock();
  }

  public Map map() {
    return new MapMock();
  }

  public GuitDom elementfactory() {
    return new GuitDomMock();
  }

  public Textarea textarea() {
    return new TextareaMock();
  }

  public Frame frame() {
    return new FrameMock();
  }

  public Form form() {
    return new FormMock();
  }

  public Param param() {
    return new ParamMock();
  }

  public A a() {
    return new AMock();
  }

  public Link link() {
    return new LinkMock();
  }

  public Span span() {
    return new SpanMock();
  }

  public H6 h6() {
    return new H6Mock();
  }

  public Title title() {
    return new TitleMock();
  }

  public H3 h3() {
    return new H3Mock();
  }

  public Div div() {
    return new DivMock();
  }

  public Input input() {
    return new InputMock();
  }

  public Script script() {
    return new ScriptMock();
  }

  public Br br() {
    return new BrMock();
  }

  public Style style() {
    return new StyleMock();
  }

  public Pre pre() {
    return new PreMock();
  }

  public Li li() {
    return new LiMock();
  }

  @Override
  public Body getDocumentBody() {
    return ElementMock.getBody();
  }

  @Override
  public HandlerRegistration addNativePreviewHandler(EventHandler handler) {
    return ElementMock.addNativePreviewHandler(handler);
  }

  @Override
  public Element element(Element e) {
    return new ElementMock("div").from(e);
  }

  @Override
  public Head head(Element e) {
    return head().from(e);
  }

  @Override
  public H2 h2(Element e) {
    return h2().from(e);
  }

  @Override
  public Label label(Element e) {
    return label().from(e);
  }

  @Override
  public Blockquote blockquote(Element e) {
    return blockquote().from(e);
  }

  @Override
  public Option option(Element e) {
    return option().from(e);
  }

  @Override
  public Legend legend(Element e) {
    return legend().from(e);
  }

  @Override
  public H4 h4(Element e) {
    return h4().from(e);
  }

  @Override
  public Select select(Element e) {
    return select().from(e);
  }

  @Override
  public Object object(Element e) {
    return object().from(e);
  }

  @Override
  public Area area(Element e) {
    return area().from(e);
  }

  @Override
  public Iframe iframe(Element e) {
    return iframe().from(e);
  }

  @Override
  public Optgroup optgroup(Element e) {
    return optgroup().from(e);
  }

  @Override
  public Button button(Element e) {
    return button().from(e);
  }

  @Override
  public Img img(Element e) {
    return img().from(e);
  }

  @Override
  public Fieldset fieldset(Element e) {
    return fieldset().from(e);
  }

  @Override
  public Dl dl(Element e) {
    return dl().from(e);
  }

  @Override
  public Q q(Element e) {
    return q().from(e);
  }

  @Override
  public Ol ol(Element e) {
    return ol().from(e);
  }

  @Override
  public H1 h1(Element e) {
    return h1().from(e);
  }

  @Override
  public Body body(Element e) {
    return body().from(e);
  }

  @Override
  public Hr hr(Element e) {
    return hr().from(e);
  }

  @Override
  public H5 h5(Element e) {
    return h5().from(e);
  }

  @Override
  public Ul ul(Element e) {
    return ul().from(e);
  }

  @Override
  public Frameset frameset(Element e) {
    return frameset().from(e);
  }

  @Override
  public Table table(Element e) {
    return table().from(e);
  }

  @Override
  public Caption caption(Element e) {
    return caption().from(e);
  }

  @Override
  public Source source(Element e) {
    return source().from(e);
  }

  @Override
  public P p(Element e) {
    return p().from(e);
  }

  @Override
  public Map map(Element e) {
    return map().from(e);
  }

  @Override
  public Textarea textarea(Element e) {
    return textarea().from(e);
  }

  @Override
  public Frame frame(Element e) {
    return frame().from(e);
  }

  @Override
  public Form form(Element e) {
    return form().from(e);
  }

  @Override
  public Param param(Element e) {
    return param().from(e);
  }

  @Override
  public A a(Element e) {
    return a().from(e);
  }

  @Override
  public Link link(Element e) {
    return link().from(e);
  }

  @Override
  public Span span(Element e) {
    return span().from(e);
  }

  @Override
  public H6 h6(Element e) {
    return h6().from(e);
  }

  @Override
  public Title title(Element e) {
    return title().from(e);
  }

  @Override
  public H3 h3(Element e) {
    return h3().from(e);
  }

  @Override
  public Div div(Element e) {
    return div().from(e);
  }

  @Override
  public Input input(Element e) {
    return input().from(e);
  }

  @Override
  public Script script(Element e) {
    return script().from(e);
  }

  @Override
  public Br br(Element e) {
    return br().from(e);
  }

  @Override
  public Style style(Element e) {
    return style().from(e);
  }

  @Override
  public Pre pre(Element e) {
    return pre().from(e);
  }

  @Override
  public Li li(Element e) {
    return li().from(e);
  }

  @Override
  public Element elementById(String id) {
    return ElementMock.getElementById(id);
  }
}
