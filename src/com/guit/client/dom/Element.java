package com.guit.client.dom;

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

import com.guit.client.Implementation;
import com.guit.client.binder.ViewAccesor;
import com.guit.client.dom.impl.ElementImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ElementMock;

import java.util.List;

@Implementation(ElementImpl.class)
@Mock(ElementMock.class)
public interface Element extends ViewAccesor {

  // Events

  HandlerRegistration click(EventHandler handler);

  HandlerRegistration dblclick(EventHandler handler);

  HandlerRegistration blur(EventHandler handler);

  HandlerRegistration focus(EventHandler handler);

  HandlerRegistration change(EventHandler handler);

  HandlerRegistration mousedown(EventHandler handler);

  HandlerRegistration mouseup(EventHandler handler);

  HandlerRegistration mousemove(EventHandler handler);

  HandlerRegistration mouseout(EventHandler handler);

  HandlerRegistration mouseover(EventHandler handler);

  HandlerRegistration keydown(EventHandler handler);

  HandlerRegistration keyup(EventHandler handler);

  HandlerRegistration keypress(EventHandler handler);

  HandlerRegistration load(EventHandler handler);

  void click();

  void dblclick();

  void blur();

  void focus();

  void change();

  void mousedown();

  void mouseup();

  void mousemove();

  void mouseout();

  void mouseover();

  void keydown();

  void keyup();

  void keypress();

  // Dom

  <T extends Element> T from(Element e);

  Element attr(String name, String value);

  Element css(String property, String value);

  Element css(String property, String value, Unit unit);

  Element html(String html);

  List<Element> children();

  Element get(int index);

  int count();

  Element insert(Element element, int index);

  Element insert(Element element, Element beforeElement);

  Element add(Element element);

  Element remove(Element element);

  Element remove(int index);

  String attr(String name);

  String css(String property);

  String html();

  String text();

  Element text(String html);

  String tag();

  List<Element> query(String selectors);

  Element addClassName(String className);

  Element removeClassName(String className);

  Element toogleClassName(String className);

  String getClassName();

  boolean hasClassName(String className);

  int absoluteBottom();

  int absoluteLeft();

  int absoluteRight();

  int absoluteTop();

  int clientHeight();

  int clientWidth();

  String dir();

  List<Element> elementsByTagName(String name);

  Element first();

  Element last();

  String id();

  String lang();

  Element nextSibling();

  Element previousSibling();

  int offsetHeight();

  int offsetLeft();

  Element parent();

  int offsetTop();

  int offsetWidth();

  boolean propertyBoolean(String name);

  double propertyDouble(String name);

  int propertyInt(String name);

  java.lang.Object propertyObject(String name);

  String propertyString(String name);

  int scrollHeight();

  int scrollLeft();

  int scrollTop();

  int scrollWidth();

  int tabIndex();

  String title();

  boolean hasAttribute(String name);

  boolean hasTagName(String tagName);

  Element removeAttr(String name);

  Element replaceClassName(String oldClassName, String newClassName);

  Element scrollIntoView();

  Element setClassName(String className);

  Element dir(String dir);

  Element id(String id);

  Element lang(String lang);

  Element propertyBoolean(String name, boolean value);

  Element propertyDouble(String name, double value);

  Element propertyInt(String name, int value);

  Element propertyObject(String name, java.lang.Object value);

  Element propertyString(String name, String value);

  Element scrollLeft(int scrollLeft);

  Element scrollTop(int scrollTop);

  Element tabIndex(int tabIndex);

  Element title(String title);

  Element removeFromParent();

  // This method is only for testing
  void scrollWidth(int scrollWidth);

  // CSS

  /**
   * Clear the background-color css property.
   */
  Element clearBackgroundColor();

  /**
   * Clear the background-image css property.
   */
  Element clearBackgroundImage();

  /**
   * Clear the border-color css property.
   */
  Element clearBorderColor();

  /**
   * Clears the border-style CSS property.
   */
  Element clearBorderStyle();

  /**
   * Clear the border-width css property.
   */
  Element clearBorderWidth();

  /**
   * Clear the bottom css property.
   */
  Element clearBottom();

  /**
   * Clear the color css property.
   */
  Element clearColor();

  /**
   * Clears the cursor CSS property.
   */
  Element clearCursor();

  /**
   * Clears the display CSS property.
   */
  Element clearDisplay();

  /**
   * Clear the font-size css property.
   */
  Element clearFloat();

  /**
   * Clear the font-size css property.
   */
  Element clearFontSize();

  /**
   * Clears the font-style CSS property.
   */
  Element clearFontStyle();

  /**
   * Clears the font-weight CSS property.
   */
  Element clearFontWeight();

  /**
   * Clear the height css property.
   */
  Element clearHeight();

  /**
   * Clear the left css property.
   */
  Element clearLeft();

  /**
   * Clears the list-style-type CSS property.
   */
  Element clearListStyleType();

  /**
   * Clear the margin css property.
   */
  Element clearMargin();

  /**
   * Clear the margin-bottom css property.
   */
  Element clearMarginBottom();

  /**
   * Clear the margin-left css property.
   */
  Element clearMarginLeft();

  /**
   * Clear the margin-right css property.
   */
  Element clearMarginRight();

  /**
   * Clear the margin-top css property.
   */
  Element clearMarginTop();

  /**
   * Clear the opacity css property.
   */
  Element clearOpacity();

  /**
   * Clears the overflow CSS property.
   */
  Element clearOverflow();

  /**
   * Clear the padding css property.
   */
  Element clearPadding();

  /**
   * Clear the padding-bottom css property.
   */
  Element clearPaddingBottom();

  /**
   * Clear the padding-left css property.
   */
  Element clearPaddingLeft();

  /**
   * Clear the padding-right css property.
   */
  Element clearPaddingRight();

  /**
   * Clear the padding-top css property.
   */
  Element clearPaddingTop();

  /**
   * Clears the position CSS property.
   */
  Element clearPosition();

  /**
   * Clears the value of a named property, causing it to revert to its default.
   */
  Element clearProperty(String name);

  /**
   * Clear the right css property.
   */
  Element clearRight();

  /**
   * Clears the text-decoration CSS property.
   */
  Element clearTextDecoration();

  /**
   * Clear the top css property.
   */
  Element clearTop();

  /**
   * Clears the visibility CSS property.
   */
  Element clearVisibility();

  /**
   * Clear the width css property.
   */
  Element clearWidth();

  /**
   * Clear the z-index css property.
   */
  Element clearZIndex();

  /**
   * Get the background-color css property.
   */
  String backgroundColor();

  /**
   * Get the background-image css property.
   */
  String backgroundImage();

  /**
   * Get the border-color css property.
   */
  String borderColor();

  /**
   * Gets the border-style CSS property.
   */
  String borderStyle();

  /**
   * Get the border-width css property.
   */
  String borderWidth();

  /**
   * Get the bottom css property.
   */
  String bottom();

  /**
   * Get the color css property.
   */
  String color();

  /**
   * Gets the cursor CSS property.
   */
  String cursor();

  /**
   * Gets the display CSS property.
   */
  String display();

  /**
   * Get the font-size css property.
   */
  String fontSize();

  /**
   * Gets the font-style CSS property.
   */
  String fontStyle();

  /**
   * Gets the font-weight CSS property.
   */
  String fontWeight();

  /**
   * Get the height css property.
   */
  String height();

  /**
   * Get the left css property.
   */
  String left();

  /**
   * Gets the list-style-type CSS property.
   */
  String listStyleType();

  /**
   * Get the margin css property.
   */
  String margin();

  /**
   * Get the margin-bottom css property.
   */
  String marginBottom();

  /**
   * Get the margin-left css property.
   */
  String marginLeft();

  /**
   * Get the margin-right css property.
   */
  String marginRight();

  /**
   * Get the margin-top css property.
   */
  String marginTop();

  /**
   * Get the opacity css property.
   */
  String opacity();

  /**
   * Gets the overflow CSS property.
   */
  String overflow();

  /**
   * Get the padding css property.
   */
  String padding();

  /**
   * Get the padding-bottom css property.
   */
  String paddingBottom();

  /**
   * Get the padding-left css property.
   */
  String paddingLeft();

  /**
   * Get the padding-right css property.
   */
  String paddingRight();

  /**
   * Get the padding-top css property.
   */
  String paddingTop();

  /**
   * Gets the position CSS property.
   */
  String position();

  /**
   * Get the right css property.
   */
  String right();

  /**
   * Gets the text-decoration CSS property.
   */
  String textDecoration();

  /**
   * Get the top css property.
   */
  String top();

  /**
   * Gets the vertical-align CSS property.
   */
  String verticalAlign();

  /**
   * Gets the visibility CSS property.
   */
  String visibility();

  /**
   * Get the width css property.
   */
  String width();

  /**
   * Get the z-index css property.
   */
  String zIndex();

  /**
   * Set the background-color css property.
   */
  Element backgroundColor(String value);

  /**
   * Set the background-image css property.
   */
  Element backgroundImage(String value);

  /**
   * Set the border-color css property.
   */
  Element borderColor(String value);

  /**
   * Sets the border-style CSS property.
   */
  Element borderStyle(BorderStyle value);

  /**
   * Set the border-width css property.
   */
  Element borderWidth(double value, Unit unit);

  /**
   * Set the bottom css property.
   */
  Element bottom(double value, Unit unit);

  /**
   * Sets the color CSS property.
   */
  Element color(String value);

  /**
   * Sets the cursor CSS property.
   */
  Element cursor(Cursor value);

  /**
   * Sets the display CSS property.
   */
  Element display(Display value);

  /**
   * Set the float css property.
   */
  Element floatTo(Float value);

  /**
   * Set the font-size css property.
   */
  Element fontSize(double value, Unit unit);

  /**
   * Sets the font-style CSS property.
   */
  Element fontStyle(FontStyle value);

  /**
   * Sets the font-weight CSS property.
   */
  Element fontWeight(FontWeight value);

  /**
   * Set the height css property.
   */
  Element height(double value, Unit unit);

  /**
   * Set the left css property.
   */
  Element left(double value, Unit unit);

  /**
   * Sets the list-style-type CSS property.
   */
  Element listStyleType(ListStyleType value);

  /**
   * Set the margin css property.
   */
  Element margin(double value, Unit unit);

  /**
   * Set the margin-bottom css property.
   */
  Element marginBottom(double value, Unit unit);

  /**
   * Set the margin-left css property.
   */
  Element marginLeft(double value, Unit unit);

  /**
   * Set the margin-right css property.
   */
  Element marginRight(double value, Unit unit);

  /**
   * Set the margin-top css property.
   */
  Element marginTop(double value, Unit unit);

  /**
   * Set the opacity css property.
   */
  Element opacity(double value);

  /**
   * Sets the overflow CSS property.
   */
  Element overflow(Overflow value);

  /**
   * Set the padding css property.
   */
  Element padding(double value, Unit unit);

  /**
   * Set the padding-bottom css property.
   */
  Element paddingBottom(double value, Unit unit);

  /**
   * Set the padding-left css property.
   */
  Element paddingLeft(double value, Unit unit);

  /**
   * Set the padding-right css property.
   */
  Element paddingRight(double value, Unit unit);

  /**
   * Set the padding-top css property.
   */
  Element paddingTop(double value, Unit unit);

  /**
   * Sets the position CSS property.
   */
  Element position(Position value);

  /**
   * Set the right css property.
   */
  Element right(double value, Unit unit);

  /**
   * Sets the text-decoration CSS property.
   */
  Element textDecoration(TextDecoration value);

  /**
   * Set the top css property.
   */
  Element top(double value, Unit unit);

  /**
   * Sets the vertical-align CSS property.
   */
  Element verticalAlign(VerticalAlign value);

  /**
   * Sets the vertical-align CSS property.
   */
  Element verticalAlign(double value, Unit unit);

  /**
   * Sets the visibility CSS property.
   */
  Element visibility(Visibility value);

  /**
   * Set the width css property.
   */
  Element width(double value, Unit unit);

  /**
   * Set the z-index css property.
   */
  Element zIndex(int value);

  void visible(boolean visible);

  boolean visible();

}