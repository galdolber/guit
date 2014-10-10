package com.guit.client.dom.impl;

import static com.google.gwt.query.client.GQuery.*;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeCollection;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import com.guit.client.dom.Element;
import com.guit.client.dom.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class ElementImpl implements Element {

  protected com.google.gwt.dom.client.Element e;

  public ElementImpl() {
    this("div");
  }

  public ElementImpl(String tagName) {
    e = DOM.createElement(tagName);
  }

  public ElementImpl(com.google.gwt.dom.client.Element e) {
    if (e == null) {
      throw new RuntimeException("Null element");
    }
    this.e = e;
  }

  protected ArrayList<Element> nodesToElements(
      NodeCollection<com.google.gwt.dom.client.Element> elements) {
    ArrayList<Element> el = new ArrayList<Element>();
    for (int i = 0; i < elements.getLength(); i++) {
      el.add(new ElementImpl(elements.getItem(i)));
    }
    return el;
  }

  @Override
  public void setTarget(java.lang.Object target) {
    if (target instanceof IsWidget) {
      e = ((IsWidget) target).asWidget().getElement();
    } else if (target instanceof Widget) {
      e = ((Widget) target).getElement();
    } else {
      e = (com.google.gwt.dom.client.Element) target;
    }
  }

  @Override
  public Element attr(String name, String value) {
    e.setAttribute(name, value);
    return this;
  }

  @Override
  public Element css(String property, String value) {
    assertCamelCase(property);
    e.getStyle().setProperty(property, value);
    return this;
  }

  @Override
  public Element css(String property, String value, Unit unit) {
    css(property, value + unit);
    return this;
  }

  @Override
  public Element html(String html) {
    e.setInnerHTML(html);
    return this;
  }

  @Override
  public List<Element> children() {
    NodeList<Node> children = e.getChildNodes();
    ArrayList<Element> c = new ArrayList<Element>();
    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.getItem(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        c.add(new ElementImpl((com.google.gwt.dom.client.Element) node));
      }
    }
    return c;
  }

  @Override
  public String attr(String name) {
    return e.getAttribute(name);
  }

  @Override
  public String css(String property) {
    return e.getStyle().getProperty(property);
  }

  @Override
  public String html() {
    return e.getInnerHTML();
  }

  @Override
  public String text() {
    return e.getInnerText();
  }

  @Override
  public Element text(String html) {
    e.setInnerText(html);
    return this;
  }

  @Override
  public String tag() {
    return e.getTagName().toLowerCase();
  }

  @Override
  public List<Element> query(String selectors) {
    ArrayList<Element> elements = new ArrayList<Element>();
    for (com.google.gwt.dom.client.Element element : GQuery.$(e).find(selectors).elements()) {
      elements.add(new ElementImpl(element));
    }
    return elements;
  }

  @Override
  public Element addClassName(String className) {
    e.addClassName(className);
    return this;
  }

  @Override
  public Element removeClassName(String className) {
    e.removeClassName(className);
    return this;
  }

  @Override
  public Element toogleClassName(String className) {
    if (hasClassName(className)) {
      removeClassName(className);
    } else {
      addClassName(className);
    }
    return this;
  }

  @Override
  public String getClassName() {
    return e.getClassName();
  }

  @Override
  public boolean hasClassName(String className) {
    return e.getClassName().contains(className);
  }

  @Override
  public int absoluteBottom() {
    return e.getAbsoluteBottom();
  }

  @Override
  public int absoluteLeft() {
    return e.getAbsoluteLeft();
  }

  @Override
  public int absoluteRight() {
    return e.getAbsoluteRight();
  }

  @Override
  public int absoluteTop() {
    return e.getAbsoluteTop();
  }

  @Override
  public int clientHeight() {
    return e.getClientHeight();
  }

  @Override
  public int clientWidth() {
    return e.getClientWidth();
  }

  @Override
  public String dir() {
    return e.getDir();
  }

  @Override
  public List<Element> elementsByTagName(String name) {
    NodeList<com.google.gwt.dom.client.Element> els = e.getElementsByTagName(name);
    ArrayList<Element> el = new ArrayList<Element>();
    for (int i = 0; i < els.getLength(); i++) {
      el.add(new ElementImpl((com.google.gwt.dom.client.Element) els.getItem(i)));
    }
    return el;
  }

  @Override
  public Element first() {
    return children().get(0);
  }

  @Override
  public Element last() {
    List<Element> children = children();
    return children.get(children.size() - 1);
  }

  @Override
  public String id() {
    return e.getId();
  }

  @Override
  public String lang() {
    return e.getLang();
  }

  @Override
  public int offsetHeight() {
    return e.getOffsetHeight();
  }

  @Override
  public int offsetLeft() {
    return e.getOffsetLeft();
  }

  @Override
  public Element parent() {
    return new ElementImpl((com.google.gwt.dom.client.Element) e.getParentElement());
  }

  @Override
  public int offsetTop() {
    return e.getOffsetTop();
  }

  @Override
  public int offsetWidth() {
    return e.getOffsetWidth();
  }

  @Override
  public boolean propertyBoolean(String name) {
    return e.getPropertyBoolean(name);
  }

  @Override
  public double propertyDouble(String name) {
    return e.getPropertyDouble(name);
  }

  @Override
  public int propertyInt(String name) {
    return e.getPropertyInt(name);
  }

  @Override
  public java.lang.Object propertyObject(String name) {
    return e.getPropertyObject(name);
  }

  @Override
  public String propertyString(String name) {
    return e.getPropertyString(name);
  }

  @Override
  public int scrollHeight() {
    return e.getScrollHeight();
  }

  @Override
  public int scrollLeft() {
    return e.getScrollLeft();
  }

  @Override
  public int scrollTop() {
    return e.getScrollTop();
  }

  @Override
  public int scrollWidth() {
    return e.getScrollWidth();
  }

  @Override
  public int tabIndex() {
    return e.getTabIndex();
  }

  @Override
  public String title() {
    return e.getTitle();
  }

  @Override
  public boolean hasAttribute(String name) {
    return e.hasAttribute(name);
  }

  @Override
  public boolean hasTagName(String tagName) {
    return e.hasTagName(tagName);
  }

  @Override
  public Element removeAttr(String name) {
    e.removeAttribute(name);
    return this;
  }

  @Override
  public Element replaceClassName(String oldClassName, String newClassName) {
    e.replaceClassName(oldClassName, newClassName);
    return this;
  }

  @Override
  public Element scrollIntoView() {
    e.scrollIntoView();
    return this;
  }

  @Override
  public Element setClassName(String className) {
    e.setClassName(className);
    return this;
  }

  @Override
  public Element dir(String dir) {
    e.setDir(dir);
    return this;
  }

  @Override
  public Element id(String id) {
    e.setId(id);
    return this;
  }

  @Override
  public Element lang(String lang) {
    e.setLang(lang);
    return this;
  }

  @Override
  public Element propertyBoolean(String name, boolean value) {
    e.setPropertyBoolean(name, value);
    return this;
  }

  @Override
  public Element propertyDouble(String name, double value) {
    e.setPropertyDouble(name, value);
    return this;
  }

  @Override
  public Element propertyInt(String name, int value) {
    e.setPropertyInt(name, value);
    return this;
  }

  @Override
  public Element propertyObject(String name, java.lang.Object value) {
    e.setPropertyObject(name, value);
    return this;
  }

  @Override
  public Element propertyString(String name, String value) {
    e.setPropertyString(name, value);
    return this;
  }

  @Override
  public Element scrollLeft(int scrollLeft) {
    e.setScrollLeft(scrollLeft);
    return this;
  }

  @Override
  public Element scrollTop(int scrollTop) {
    e.setScrollTop(scrollTop);
    return this;
  }

  @Override
  public Element tabIndex(int tabIndex) {
    e.setTabIndex(tabIndex);
    return this;
  }

  @Override
  public Element title(String title) {
    e.setTitle(title);
    return this;
  }

  @Override
  public void scrollWidth(int scrollWidth) {
    // This is only for testing
  }

  @Override
  public Element insert(Element element, int index) {
    NodeList<Node> children = e.getChildNodes();
    int pos = 0;
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.getItem(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (index == pos) {
          e.insertBefore(((ElementImpl) element).e, n);
          break;
        }

        pos++;
      }
    }
    return this;
  }

  @Override
  public Element add(Element element) {
    e.appendChild(((ElementImpl) element).e);
    return this;
  }

  @Override
  public Element remove(Element element) {
    e.removeChild(((ElementImpl) element).e);
    return this;
  }

  @Override
  public Element remove(int index) {
    remove(children().get(index));
    return this;
  }

  @Override
  public Element removeFromParent() {
    e.removeFromParent();
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Element> T from(Element e) {
    this.e = ((ElementImpl) e).e;
    return (T) this;
  }

  @Override
  public Element insert(Element element, Element beforeElement) {
    insert(element, children().indexOf(beforeElement)); // Fix bug when wrapping
                                                        // child element
    return this;
  }

  @Override
  public HandlerRegistration click(final EventHandler handler) {
    $(e).click(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONCLICK);
      }
    };
  }

  @Override
  public HandlerRegistration dblclick(final EventHandler handler) {
    $(e).dblclick(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONDBLCLICK);
      }
    };
  }

  @Override
  public HandlerRegistration blur(final EventHandler handler) {
    $(e).blur(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONBLUR);
      }
    };
  }

  @Override
  public HandlerRegistration focus(final EventHandler handler) {
    $(e).focus(new Function() {
      @Override
      public void f() {
      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONFOCUS);
      }
    };
  }

  @Override
  public HandlerRegistration change(final EventHandler handler) {
    $(e).change(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONCHANGE);
      }
    };
  }

  @Override
  public HandlerRegistration mousedown(final EventHandler handler) {
    $(e).mousedown(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONMOUSEDOWN);
      }
    };
  }

  @Override
  public HandlerRegistration mouseup(final EventHandler handler) {
    $(e).mouseup(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONMOUSEUP);
      }
    };
  }

  @Override
  public HandlerRegistration mousemove(final EventHandler handler) {
    $(e).mousemove(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONMOUSEMOVE);
      }
    };
  }

  @Override
  public HandlerRegistration mouseout(final EventHandler handler) {
    $(e).mouseout(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONMOUSEOUT);
      }
    };
  }

  @Override
  public HandlerRegistration mouseover(final EventHandler handler) {
    $(e).mouseover(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONMOUSEOVER);
      }
    };
  }

  @Override
  public HandlerRegistration keydown(final EventHandler handler) {
    $(e).keydown(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONKEYDOWN);
      }
    };
  }

  @Override
  public HandlerRegistration keyup(final EventHandler handler) {
    $(e).keyup(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONKEYUP);
      }
    };
  }

  @Override
  public HandlerRegistration keypress(final EventHandler handler) {
    $(e).keypress(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONKEYPRESS);
      }
    };
  }

  @SuppressWarnings("deprecation")
  @Override
  public HandlerRegistration load(final EventHandler handler) {
    $(e).load(new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(com.google.gwt.user.client.Event.ONLOAD);
      }
    };
  }

  @Override
  public void click() {
    $(e).click();
  }

  @Override
  public void dblclick() {
    $(e).dblclick();
  }

  @Override
  public void blur() {
    e.blur();
  }

  @Override
  public void focus() {
    e.focus();
  }

  @Override
  public void change() {
    $(e).change();
  }

  @Override
  public void mousedown() {
    $(e).mousedown();
  }

  @Override
  public void mouseup() {
    $(e).mouseup();
  }

  @Override
  public void mousemove() {
    $(e).mousemove();
  }

  @Override
  public void mouseout() {
    $(e).mouseout();
  }

  @Override
  public void mouseover() {
    $(e).mouseover();
  }

  @Override
  public void keydown() {
    $(e).keydown();
  }

  @Override
  public void keyup() {
    $(e).keyup();
  }

  @Override
  public void keypress() {
    $(e).keypress();
  }

  @Override
  public Element nextSibling() {
    com.google.gwt.dom.client.Element n = e.getNextSiblingElement();
    if (n == null) {
      return null;
    }
    return new ElementImpl((com.google.gwt.dom.client.Element) n);
  }

  @Override
  public Element previousSibling() {
    Node p = e.getPreviousSibling();
    while (p != null && p.getNodeType() != Node.ELEMENT_NODE) {
      p = p.getPreviousSibling();
    }
    if (p == null) {
      return null;
    }
    return new ElementImpl((com.google.gwt.dom.client.Element) p);
  }

  private static final String STYLE_Z_INDEX = "zIndex";
  private static final String STYLE_WIDTH = "width";
  private static final String STYLE_VISIBILITY = "visibility";
  private static final String STYLE_TOP = "top";
  private static final String STYLE_TEXT_DECORATION = "textDecoration";
  private static final String STYLE_RIGHT = "right";
  private static final String STYLE_POSITION = "position";
  private static final String STYLE_PADDING_TOP = "paddingTop";
  private static final String STYLE_PADDING_RIGHT = "paddingRight";
  private static final String STYLE_PADDING_LEFT = "paddingLeft";
  private static final String STYLE_PADDING_BOTTOM = "paddingBottom";
  private static final String STYLE_PADDING = "padding";
  private static final String STYLE_OVERFLOW = "overflow";
  private static final String STYLE_OPACITY = "opacity";
  private static final String STYLE_MARGIN_TOP = "marginTop";
  private static final String STYLE_MARGIN_RIGHT = "marginRight";
  private static final String STYLE_MARGIN_LEFT = "marginLeft";
  private static final String STYLE_MARGIN_BOTTOM = "marginBottom";
  private static final String STYLE_MARGIN = "margin";
  private static final String STYLE_LIST_STYLE_TYPE = "listStyleType";
  private static final String STYLE_LEFT = "left";
  private static final String STYLE_HEIGHT = "height";
  private static final String STYLE_FONT_WEIGHT = "fontWeight";
  private static final String STYLE_FONT_STYLE = "fontStyle";
  private static final String STYLE_FONT_SIZE = "fontSize";
  private static final String STYLE_DISPLAY = "display";
  private static final String STYLE_CURSOR = "cursor";
  private static final String STYLE_COLOR = "color";
  private static final String STYLE_BOTTOM = "bottom";
  private static final String STYLE_BORDER_WIDTH = "borderWidth";
  private static final String STYLE_BORDER_STYLE = "borderStyle";
  private static final String STYLE_BORDER_COLOR = "borderColor";
  private static final String STYLE_BACKGROUND_IMAGE = "backgroundImage";
  private static final String STYLE_BACKGROUND_COLOR = "backgroundColor";
  private static final String STYLE_VERTICAL_ALIGN = "verticalAlign";

  /**
   * Clear the background-color css property.
   */
  @Override
  public final Element clearBackgroundColor() {
    clearProperty(STYLE_BACKGROUND_COLOR);
    return this;
  }

  /**
   * Clear the background-image css property.
   */
  @Override
  public final Element clearBackgroundImage() {
    clearProperty(STYLE_BACKGROUND_IMAGE);
    return this;
  }

  /**
   * Clear the border-color css property.
   */
  @Override
  public final Element clearBorderColor() {
    clearProperty(STYLE_BORDER_COLOR);
    return this;
  }

  /**
   * Clears the border-style CSS property.
   */
  @Override
  public final Element clearBorderStyle() {
    clearProperty(STYLE_BORDER_STYLE);
    return this;
  }

  /**
   * Clear the border-width css property.
   */
  @Override
  public final Element clearBorderWidth() {
    clearProperty(STYLE_BORDER_WIDTH);
    return this;
  }

  /**
   * Clear the bottom css property.
   */
  @Override
  public final Element clearBottom() {
    clearProperty(STYLE_BOTTOM);
    return this;
  }

  /**
   * Clear the color css property.
   */
  @Override
  public final Element clearColor() {
    clearProperty(STYLE_COLOR);
    return this;
  }

  /**
   * Clears the cursor CSS property.
   */
  @Override
  public final Element clearCursor() {
    clearProperty(STYLE_CURSOR);
    return this;
  }

  /**
   * Clears the display CSS property.
   */
  @Override
  public final Element clearDisplay() {
    clearProperty(STYLE_DISPLAY);
    return this;
  }

  /**
   * Clear the font-size css property.
   */
  @Override
  public final Element clearFloat() {
    e.getStyle().clearFloat();
    return this;
  }

  /**
   * Clear the font-size css property.
   */
  @Override
  public final Element clearFontSize() {
    clearProperty(STYLE_FONT_SIZE);
    return this;
  }

  /**
   * Clears the font-style CSS property.
   */
  @Override
  public final Element clearFontStyle() {
    clearProperty(STYLE_FONT_STYLE);
    return this;
  }

  /**
   * Clears the font-weight CSS property.
   */
  @Override
  public final Element clearFontWeight() {
    clearProperty(STYLE_FONT_WEIGHT);
    return this;
  }

  /**
   * Clear the height css property.
   */
  @Override
  public final Element clearHeight() {
    clearProperty(STYLE_HEIGHT);
    return this;
  }

  /**
   * Clear the left css property.
   */
  @Override
  public final Element clearLeft() {
    clearProperty(STYLE_LEFT);
    return this;
  }

  /**
   * Clears the list-style-type CSS property.
   */
  @Override
  public final Element clearListStyleType() {
    clearProperty(STYLE_LIST_STYLE_TYPE);
    return this;
  }

  /**
   * Clear the margin css property.
   */
  @Override
  public final Element clearMargin() {
    clearProperty(STYLE_MARGIN);
    return this;
  }

  /**
   * Clear the margin-bottom css property.
   */
  @Override
  public final Element clearMarginBottom() {
    clearProperty(STYLE_MARGIN_BOTTOM);
    return this;
  }

  /**
   * Clear the margin-left css property.
   */
  @Override
  public final Element clearMarginLeft() {
    clearProperty(STYLE_MARGIN_LEFT);
    return this;
  }

  /**
   * Clear the margin-right css property.
   */
  @Override
  public final Element clearMarginRight() {
    clearProperty(STYLE_MARGIN_RIGHT);
    return this;
  }

  /**
   * Clear the margin-top css property.
   */
  @Override
  public final Element clearMarginTop() {
    clearProperty(STYLE_MARGIN_TOP);
    return this;
  }

  /**
   * Clear the opacity css property.
   */
  @Override
  public final Element clearOpacity() {
    e.getStyle().clearOpacity();
    return this;
  }

  /**
   * Clears the overflow CSS property.
   */
  @Override
  public final Element clearOverflow() {
    clearProperty(STYLE_OVERFLOW);
    return this;
  }

  /**
   * Clear the padding css property.
   */
  @Override
  public final Element clearPadding() {
    clearProperty(STYLE_PADDING);
    return this;
  }

  /**
   * Clear the padding-bottom css property.
   */
  @Override
  public final Element clearPaddingBottom() {
    clearProperty(STYLE_PADDING_BOTTOM);
    return this;
  }

  /**
   * Clear the padding-left css property.
   */
  @Override
  public final Element clearPaddingLeft() {
    clearProperty(STYLE_PADDING_LEFT);
    return this;
  }

  /**
   * Clear the padding-right css property.
   */
  @Override
  public final Element clearPaddingRight() {
    clearProperty(STYLE_PADDING_RIGHT);
    return this;
  }

  /**
   * Clear the padding-top css property.
   */
  @Override
  public final Element clearPaddingTop() {
    clearProperty(STYLE_PADDING_TOP);
    return this;
  }

  /**
   * Clears the position CSS property.
   */
  @Override
  public final Element clearPosition() {
    clearProperty(STYLE_POSITION);
    return this;
  }

  /**
   * Clears the value of a named property, causing it to revert to its default.
   */
  @Override
  public final Element clearProperty(String name) {
    css(name, "");
    return this;
  }

  /**
   * Clear the right css property.
   */
  @Override
  public final Element clearRight() {
    clearProperty(STYLE_RIGHT);
    return this;
  }

  /**
   * Clears the text-decoration CSS property.
   */
  @Override
  public final Element clearTextDecoration() {
    clearProperty(STYLE_TEXT_DECORATION);
    return this;
  }

  /**
   * Clear the top css property.
   */
  @Override
  public final Element clearTop() {
    clearProperty(STYLE_TOP);
    return this;
  }

  /**
   * Clears the visibility CSS property.
   */
  @Override
  public final Element clearVisibility() {
    clearProperty(STYLE_VISIBILITY);
    return this;
  }

  /**
   * Clear the width css property.
   */
  @Override
  public final Element clearWidth() {
    clearProperty(STYLE_WIDTH);
    return this;
  }

  /**
   * Clear the z-index css property.
   */
  @Override
  public final Element clearZIndex() {
    clearProperty(STYLE_Z_INDEX);
    return this;
  }

  /**
   * Get the background-color css property.
   */
  @Override
  public final String backgroundColor() {
    return css(STYLE_BACKGROUND_COLOR);
  }

  /**
   * Get the background-image css property.
   */
  @Override
  public final String backgroundImage() {
    return css(STYLE_BACKGROUND_IMAGE);
  }

  /**
   * Get the border-color css property.
   */
  @Override
  public final String borderColor() {
    return css(STYLE_BORDER_COLOR);
  }

  /**
   * Gets the border-style CSS property.
   */
  @Override
  public final String borderStyle() {
    return css(STYLE_BORDER_STYLE);
  }

  /**
   * Get the border-width css property.
   */
  @Override
  public final String borderWidth() {
    return css(STYLE_BORDER_WIDTH);
  }

  /**
   * Get the bottom css property.
   */
  @Override
  public final String bottom() {
    return css(STYLE_BOTTOM);
  }

  /**
   * Get the color css property.
   */
  @Override
  public final String color() {
    return css(STYLE_COLOR);
  }

  /**
   * Gets the cursor CSS property.
   */
  @Override
  public final String cursor() {
    return css(STYLE_CURSOR);
  }

  /**
   * Gets the display CSS property.
   */
  @Override
  public final String display() {
    return css(STYLE_DISPLAY);
  }

  /**
   * Get the font-size css property.
   */
  @Override
  public final String fontSize() {
    return css(STYLE_FONT_SIZE);
  }

  /**
   * Gets the font-style CSS property.
   */
  @Override
  public final String fontStyle() {
    return css(STYLE_FONT_STYLE);
  }

  /**
   * Gets the font-weight CSS property.
   */
  @Override
  public final String fontWeight() {
    return css(STYLE_FONT_WEIGHT);
  }

  /**
   * Get the height css property.
   */
  @Override
  public final String height() {
    return css(STYLE_HEIGHT);
  }

  /**
   * Get the left css property.
   */
  @Override
  public final String left() {
    return css(STYLE_LEFT);
  }

  /**
   * Gets the list-style-type CSS property.
   */
  @Override
  public final String listStyleType() {
    return css(STYLE_LIST_STYLE_TYPE);
  }

  /**
   * Get the margin css property.
   */
  @Override
  public final String margin() {
    return css(STYLE_MARGIN);
  }

  /**
   * Get the margin-bottom css property.
   */
  @Override
  public final String marginBottom() {
    return css(STYLE_MARGIN_BOTTOM);
  }

  /**
   * Get the margin-left css property.
   */
  @Override
  public final String marginLeft() {
    return css(STYLE_MARGIN_LEFT);
  }

  /**
   * Get the margin-right css property.
   */
  @Override
  public final String marginRight() {
    return css(STYLE_MARGIN_RIGHT);
  }

  /**
   * Get the margin-top css property.
   */
  @Override
  public final String marginTop() {
    return css(STYLE_MARGIN_TOP);
  }

  /**
   * Get the opacity css property.
   */
  @Override
  public final String opacity() {
    return css(STYLE_OPACITY);
  }

  /**
   * Gets the overflow CSS property.
   */
  @Override
  public final String overflow() {
    return css(STYLE_OVERFLOW);
  }

  /**
   * Get the padding css property.
   */
  @Override
  public final String padding() {
    return css(STYLE_PADDING);
  }

  /**
   * Get the padding-bottom css property.
   */
  @Override
  public final String paddingBottom() {
    return css(STYLE_PADDING_BOTTOM);
  }

  /**
   * Get the padding-left css property.
   */
  @Override
  public final String paddingLeft() {
    return css(STYLE_PADDING_LEFT);
  }

  /**
   * Get the padding-right css property.
   */
  @Override
  public final String paddingRight() {
    return css(STYLE_PADDING_RIGHT);
  }

  /**
   * Get the padding-top css property.
   */
  @Override
  public final String paddingTop() {
    return css(STYLE_PADDING_TOP);
  }

  /**
   * Gets the position CSS property.
   */
  @Override
  public final String position() {
    return css(STYLE_POSITION);
  }

  /**
   * Get the right css property.
   */
  @Override
  public final String right() {
    return css(STYLE_RIGHT);
  }

  /**
   * Gets the text-decoration CSS property.
   */
  @Override
  public final String textDecoration() {
    return css(STYLE_TEXT_DECORATION);
  }

  /**
   * Get the top css property.
   */
  @Override
  public final String top() {
    return css(STYLE_TOP);
  }

  /**
   * Gets the vertical-align CSS property.
   */
  @Override
  public final String verticalAlign() {
    return css(STYLE_VERTICAL_ALIGN);
  }

  /**
   * Gets the visibility CSS property.
   */
  @Override
  public final String visibility() {
    return css(STYLE_VISIBILITY);
  }

  /**
   * Get the width css property.
   */
  @Override
  public final String width() {
    return css(STYLE_WIDTH);
  }

  /**
   * Get the z-index css property.
   */
  @Override
  public final String zIndex() {
    return css(STYLE_Z_INDEX);
  }

  /**
   * Set the background-color css property.
   */
  @Override
  public final Element backgroundColor(String value) {
    return css(STYLE_BACKGROUND_COLOR, value);
  }

  /**
   * Set the background-image css property.
   */
  @Override
  public final Element backgroundImage(String value) {
    return css(STYLE_BACKGROUND_IMAGE, value);
  }

  /**
   * Set the border-color css property.
   */
  @Override
  public final Element borderColor(String value) {
    return css(STYLE_BORDER_COLOR, value);
  }

  /**
   * Sets the border-style CSS property.
   */
  @Override
  public final Element borderStyle(BorderStyle value) {
    return css(STYLE_BORDER_STYLE, value.getCssName());
  }

  /**
   * Set the border-width css property.
   */
  @Override
  public final Element borderWidth(double value, Unit unit) {
    return css(STYLE_BORDER_WIDTH, value, unit);
  }

  /**
   * Set the bottom css property.
   */
  @Override
  public final Element bottom(double value, Unit unit) {
    return css(STYLE_BOTTOM, value, unit);
  }

  /**
   * Sets the color CSS property.
   */
  @Override
  public final Element color(String value) {
    return css(STYLE_COLOR, value);
  }

  /**
   * Sets the cursor CSS property.
   */
  @Override
  public final Element cursor(Cursor value) {
    return css(STYLE_CURSOR, value.getCssName());
  }

  /**
   * Sets the display CSS property.
   */
  @Override
  public final Element display(Display value) {
    return css(STYLE_DISPLAY, value.getCssName());
  }

  /**
   * Set the float css property.
   */
  @Override
  public final Element floatTo(Float value) {
    e.getStyle().setFloat(value);
    return this;
  }

  /**
   * Set the font-size css property.
   */
  @Override
  public final Element fontSize(double value, Unit unit) {
    return css(STYLE_FONT_SIZE, value, unit);
  }

  /**
   * Sets the font-style CSS property.
   */
  @Override
  public final Element fontStyle(FontStyle value) {
    return css(STYLE_FONT_STYLE, value.getCssName());
  }

  /**
   * Sets the font-weight CSS property.
   */
  @Override
  public final Element fontWeight(FontWeight value) {
    return css(STYLE_FONT_WEIGHT, value.getCssName());
  }

  /**
   * Set the height css property.
   */
  @Override
  public final Element height(double value, Unit unit) {
    return css(STYLE_HEIGHT, value, unit);
  }

  /**
   * Set the left css property.
   */
  @Override
  public final Element left(double value, Unit unit) {
    return css(STYLE_LEFT, value, unit);
  }

  /**
   * Sets the list-style-type CSS property.
   */
  @Override
  public final Element listStyleType(ListStyleType value) {
    return css(STYLE_LIST_STYLE_TYPE, value.getCssName());
  }

  /**
   * Set the margin css property.
   */
  @Override
  public final Element margin(double value, Unit unit) {
    return css(STYLE_MARGIN, value, unit);
  }

  /**
   * Set the margin-bottom css property.
   */
  @Override
  public final Element marginBottom(double value, Unit unit) {
    return css(STYLE_MARGIN_BOTTOM, value, unit);
  }

  /**
   * Set the margin-left css property.
   */
  @Override
  public final Element marginLeft(double value, Unit unit) {
    return css(STYLE_MARGIN_LEFT, value, unit);
  }

  /**
   * Set the margin-right css property.
   */
  @Override
  public final Element marginRight(double value, Unit unit) {
    return css(STYLE_MARGIN_RIGHT, value, unit);
  }

  /**
   * Set the margin-top css property.
   */
  @Override
  public final Element marginTop(double value, Unit unit) {
    return css(STYLE_MARGIN_TOP, value, unit);
  }

  /**
   * Set the opacity css property.
   */
  @Override
  public final Element opacity(double value) {
    e.getStyle().setOpacity(value);
    return this;
  }

  /**
   * Sets the overflow CSS property.
   */
  @Override
  public final Element overflow(Overflow value) {
    return css(STYLE_OVERFLOW, value.getCssName());
  }

  /**
   * Set the padding css property.
   */
  @Override
  public final Element padding(double value, Unit unit) {
    return css(STYLE_PADDING, value, unit);
  }

  /**
   * Set the padding-bottom css property.
   */
  @Override
  public final Element paddingBottom(double value, Unit unit) {
    return css(STYLE_PADDING_BOTTOM, value, unit);
  }

  /**
   * Set the padding-left css property.
   */
  @Override
  public final Element paddingLeft(double value, Unit unit) {
    return css(STYLE_PADDING_LEFT, value, unit);
  }

  /**
   * Set the padding-right css property.
   */
  @Override
  public final Element paddingRight(double value, Unit unit) {
    return css(STYLE_PADDING_RIGHT, value, unit);
  }

  /**
   * Set the padding-top css property.
   */
  @Override
  public final Element paddingTop(double value, Unit unit) {
    return css(STYLE_PADDING_TOP, value, unit);
  }

  /**
   * Sets the position CSS property.
   */
  @Override
  public final Element position(Position value) {
    return css(STYLE_POSITION, value.getCssName());
  }

  /**
   * Sets the value of a named property, in pixels.
   * 
   * This is shorthand for <code>value + "px"</code>.
   */
  public final Element css(String name, int value) {
    return css(name, value, Unit.PX);
  }

  /**
   * Sets the value of a named property in the specified units.
   */
  public Element css(String name, double value, Unit unit) {
    return css(name, value + unit.getType());
  }

  /**
   * Set the right css property.
   */
  @Override
  public final Element right(double value, Unit unit) {
    return css(STYLE_RIGHT, value, unit);
  }

  /**
   * Sets the text-decoration CSS property.
   */
  @Override
  public final Element textDecoration(TextDecoration value) {
    return css(STYLE_TEXT_DECORATION, value.getCssName());
  }

  /**
   * Set the top css property.
   */
  @Override
  public final Element top(double value, Unit unit) {
    return css(STYLE_TOP, value, unit);
  }

  /**
   * Sets the vertical-align CSS property.
   */
  @Override
  public final Element verticalAlign(VerticalAlign value) {
    return css(STYLE_VERTICAL_ALIGN, value.getCssName());
  }

  /**
   * Sets the vertical-align CSS property.
   */
  @Override
  public final Element verticalAlign(double value, Unit unit) {
    return css(STYLE_VERTICAL_ALIGN, value, unit);
  }

  /**
   * Sets the visibility CSS property.
   */
  @Override
  public final Element visibility(Visibility value) {
    return css(STYLE_VISIBILITY, value.getCssName());
  }

  /**
   * Set the width css property.
   */
  @Override
  public final Element width(double value, Unit unit) {
    return css(STYLE_WIDTH, value, unit);
  }

  /**
   * Set the z-index css property.
   */
  @Override
  public final Element zIndex(int value) {
    return css(STYLE_Z_INDEX, value + "");
  }

  /**
   * Assert that the specified property does not contain a hyphen.
   * 
   * @param name the property name
   */
  private void assertCamelCase(String name) {
    assert !name.contains("-") : "The style name '" + name + "' should be in camelCase format";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((e == null) ? 0 : e.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    ElementImpl other = (ElementImpl) obj;
    if (e == null) {
      if (other.e != null)
        return false;
    } else if (!e.equals(other.e))
      return false;
    return true;
  }

  @Override
  public void visible(boolean visible) {
    UIObject.setVisible(e, visible);
  }

  @Override
  public boolean visible() {
    return UIObject.isVisible(e);
  }

  @Override
  public Element get(int index) {
    if (count() <= index) {
      throw new IndexOutOfBoundsException();
    }
    return new ElementImpl((com.google.gwt.dom.client.Element) e.getChild(index));
  }

  @Override
  public int count() {
    return e.getChildCount();
  }

  @Override
  public HandlerRegistration touchstart(final EventHandler handler) {
    return bindEvent(handler, "touchstart");
  }

  private HandlerRegistration bindEvent(final EventHandler handler, final String eventType) {
    $(e).bind(eventType, null, new Function() {
      @Override
      public void f() {

      }

      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        handler.onEvent(new EventImpl(e));
        return super.f(e);
      }
    });
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        $(e).unbind(eventType);
      }
    };
  }

  @Override
  public HandlerRegistration touchmove(EventHandler handler) {
    return bindEvent(handler, "touchmove");
  }

  @Override
  public HandlerRegistration touchcancel(EventHandler handler) {
    return bindEvent(handler, "touchcancel");
  }

  @Override
  public HandlerRegistration touchend(EventHandler handler) {
    return bindEvent(handler, "touchend");
  }
}
