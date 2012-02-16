package com.guit.client.dom;

import com.guit.junit.dom.DivMock;
import com.guit.junit.dom.ElementMock;
import com.guit.junit.dom.FormMock;
import com.guit.junit.dom.GuitDomMock;

import static junit.framework.Assert.*;

import org.junit.Test;

import java.util.List;

public class ElementGwtTest {

  GuitDom f = new GuitDomMock();

  @Test
  public void testBody() {
    Body body = f.getDocumentBody();
    String name = "name";
    String gal = "gal";
    body.attr(name, gal);
    assertEquals(gal, f.getDocumentBody().attr(name));
  }

  @Test
  public void complexSelector() {
    Div e = f.div();
    e.html("<input name='name' />");

    Element el = f.element("span");
    el.attr("name", "name");
    Span span = f.span().from(el);
    assertEquals("name", span.attr("name"));

    List<Element> r = e.query("input[name='name']");
    assertEquals(1, r.size());
    assertEquals("input", r.get(0).tag());
  }

  @Test(expected = RuntimeException.class)
  public void childrenException() {
    Element e = new ElementMock("input");
    e.html("<sp");
    e.children();
  }

  @Test
  public void test() {
    Element e = new ElementMock("input");
    assertTrue(e.hasTagName("input"));
    assertFalse(e.hasTagName("label"));
    assertEquals("input", e.tag());

    // Empty children
    assertEquals(0, e.children().size());

    // Attribute
    String attrName = "name";
    String attrValue = "firstName";
    e.attr(attrName, attrValue);
    assertEquals(attrValue, e.attr(attrName));
    assertTrue(e.hasAttribute(attrName));
    e.removeAttr(attrName);
    assertFalse(e.hasAttribute(attrName));

    // Style
    String styleProp = "backgroundColor";
    String styleValue = "red";
    e.css(styleProp, styleValue);
    assertEquals(styleValue, e.css(styleProp));

    styleProp = "color";
    styleValue = "red";
    e.css(styleProp, styleValue);
    assertEquals(styleValue, e.css(styleProp));
    styleValue = "black";
    e.css(styleProp, styleValue);
    assertEquals(styleValue, e.css(styleProp));

    assertTrue(e.css("border").isEmpty());

    e.attr("style", ";color:red;;");
    assertEquals("red", e.css("color"));

    // Inner html
    String childrenAttrValue = "nameLabel";
    String labelText = "First name:";
    String inputText = "<span>Insert your first <span>name</span></span>";
    String html =
        "<label " + attrName + "=\"" + childrenAttrValue + "\">" + labelText + "</label>"
            + "<input " + attrName + "=\"" + attrValue + "\">" + inputText + "</input>";
    e.html(html);
    assertEquals(html, e.html());

    // Elements by tagName
    List<Element> elementsByTagName = e.elementsByTagName("span");
    assertEquals(2, elementsByTagName.size());

    // First
    e.first();

    // Last
    e.last();

    // List children
    List<Element> children = e.children();
    assertEquals(2, children.size());

    Element labelChild = children.get(0);
    assertEquals(e, labelChild.parent());
    assertEquals("label", labelChild.tag());
    assertEquals(labelText, labelChild.html());
    assertEquals(childrenAttrValue, labelChild.attr(attrName));

    Element inputChild = children.get(1);
    assertEquals(inputText, inputChild.html());
    assertEquals("input", inputChild.tag());
    assertEquals(attrValue, inputChild.attr(attrName));

    // CSS selector
    List<Element> selected = e.query("label");
    assertEquals(1, selected.size());
  }

  @Test(expected = RuntimeException.class)
  public void queryException() {
    Element e = new ElementMock("div");
    e.html("<span");
    List<Element> emptyQuery = e.query("label");
    assertEquals(0, emptyQuery.size());
  }

  @Test
  public void emptyQuery() {
    Element e = new ElementMock("div");
    List<Element> emptyQuery = e.query("label");
    assertEquals(0, emptyQuery.size());
  }

  private boolean value = false;

  @Test
  public void callers() {
    Element e = new ElementMock("div");
    e.blur(new EventHandler() {
      @Override
      public void onEvent(Event event) {
        value = true;
      }
    });
    assertFalse(value);
    e.blur();
    assertTrue(value);
    e.focus();
    e.scrollIntoView();
  }

  @Test
  public void testTable() {
    Form e = new FormMock();
    Div div = new DivMock();
    e.add(div);
    e.html("<span><div><input id='12'></input></div></span>" + e.html());
    System.out.println(e.html());
  }

  @Test
  public void className() {
    Element e = new ElementMock("div");

    String name1 = "class1";
    String name2 = "class2";
    String name3 = "class3";

    e.addClassName(name1);
    e.addClassName(name1);
    assertTrue(e.hasClassName(name1));
    e.removeClassName(name1);
    e.removeClassName(name1);
    assertFalse(e.hasClassName(name1));
    e.toogleClassName(name1);
    assertTrue(e.hasClassName(name1));
    e.toogleClassName(name1);
    assertFalse(e.hasClassName(name1));

    e.addClassName(name1);
    e.addClassName(name2);
    assertTrue(e.hasClassName(name1));
    assertTrue(e.hasClassName(name2));
    assertEquals(name1 + " " + name2, e.getClassName());

    e.replaceClassName(name2, name3);
    assertTrue(e.hasClassName(name1));
    assertTrue(e.hasClassName(name3));
    assertFalse(e.hasClassName(name2));
  }

  @Test
  public void properties() {
    Element e = new ElementMock("div");
    String value = "value";
    e.lang(value);
    assertEquals(value, e.lang());
    e.title(value);
    assertEquals(value, e.title());
    e.dir(value);
    assertEquals(value, e.dir());
    e.id(value);
    assertEquals(value, e.id());

    int c = 10;
    e.scrollLeft(c);
    assertEquals(c, e.scrollLeft());
    e.scrollTop(c);
    assertEquals(c, e.scrollTop());
    e.scrollWidth(c);
    assertEquals(c, e.scrollWidth());
    e.tabIndex(c);
    assertEquals(c, e.tabIndex());

    e.absoluteBottom();
    e.absoluteLeft();
    e.absoluteRight();
    e.absoluteTop();
    e.clientHeight();
    e.clientWidth();
    e.offsetHeight();
    e.offsetLeft();
    e.offsetTop();
    e.offsetWidth();
    e.scrollHeight();

    String propName = "prop";
    e.propertyBoolean(propName, true);
    assertTrue(e.propertyBoolean(propName));

    double doubleValue = 1.1;
    e.propertyDouble(propName, doubleValue);
    assertEquals(doubleValue, e.propertyDouble(propName));

    e.propertyObject(propName, this);
    assertEquals(this, e.propertyObject(propName));
  }

  @Test
  public void nextSibling() {
    Element e = new ElementMock("div");
    e.html("<span></span><label style='color:red'></label>");
    Element label = e.first().nextSibling();
    assertEquals("label", label.tag());
    assertEquals("red", label.color());
  }

  @Test
  public void styleWithoutValue() {
    Element e = new ElementMock("div");
    e.attr("style", "color");
    e.attr("style", "background-color:");
    e.css("font", "arial");
    assertEquals("", e.css("color"));
    assertEquals("", e.css("background-color"));
  }

  @Test
  public void addChild() {
    String name = "name";
    String firstName = "firstName";
    String lastName = "lastName";
    String span = "span";
    String div = "div";

    Element e = new ElementMock(div);

    Element c = new ElementMock(span);
    c.attr(name, firstName);

    e.add(c);

    List<Element> children = e.children();
    assertEquals(1, children.size());
    Element retrivedSpan = children.get(0);
    assertEquals(span, retrivedSpan.tag());
    assertEquals(firstName, retrivedSpan.attr(name));

    Element d = new ElementMock(div);
    d.attr(name, lastName);

    e.add(d);

    children = e.children();
    assertEquals(2, children.size());
    Element retrivedDiv = children.get(1);
    assertEquals(div, retrivedDiv.tag());
    assertEquals(lastName, retrivedDiv.attr(name));

    // Remove

    e.remove(d);
    assertEquals(1, e.children().size());

    e.add(d);
    assertEquals(2, e.children().size());

    e.remove(1);
    assertEquals(1, e.children().size());
  }

  @Test
  public void addInsert() {
    String name = "name";
    String firstName = "firstName";
    String lastName = "lastName";
    String phone = "phone";
    String span = "span";
    String div = "div";

    Element e = new ElementMock(div);

    Element c = new ElementMock(span);
    c.attr(name, firstName);

    e.insert(c, 0);

    List<Element> children = e.children();
    assertEquals(1, children.size());
    Element retrivedSpan = children.get(0);
    assertEquals(span, retrivedSpan.tag());
    assertEquals(firstName, retrivedSpan.attr(name));

    Element d = new ElementMock(div);
    d.attr(name, lastName);

    e.insert(d, 1);

    children = e.children();
    assertEquals(2, children.size());
    Element retrivedDiv = children.get(1);
    assertEquals(div, retrivedDiv.tag());
    assertEquals(lastName, retrivedDiv.attr(name));

    Element k = new ElementMock(div);
    k.attr(name, phone);

    e.insert(k, 1);

    children = e.children();
    assertEquals(3, children.size());
    retrivedDiv = children.get(1);
    assertEquals(div, retrivedDiv.tag());
    assertEquals(phone, retrivedDiv.attr(name));
    retrivedDiv = children.get(2);
    assertEquals(div, retrivedDiv.tag());
    assertEquals(lastName, retrivedDiv.attr(name));
  }
}
