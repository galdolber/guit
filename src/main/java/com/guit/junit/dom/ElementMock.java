package com.guit.junit.dom;

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

import com.guit.client.dom.Element;
import com.guit.client.dom.Event;
import com.guit.client.dom.EventHandler;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.DOMNodeSelector;

public class ElementMock implements Element {

  private static ArrayList<EventHandler> previewHandlers = new ArrayList<EventHandler>();

  public static Document document;

  private HashMap<String, java.lang.Object> properties = new HashMap<String, Object>();
  private HashMap<String, EventHandler> handlers = new HashMap<String, EventHandler>();

  static {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      document =
          docBuilder.parse(new ByteArrayInputStream(
              ("<html><head></head><body style='padding:0;margin:0'></body></html>")
              .getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    html = new ElementMock(document.getDocumentElement());

    body =
        new BodyMock().from(new ElementMock((org.w3c.dom.Element) document.getElementsByTagName(
            "body").item(0)));
    head =
        new HeadMock().from(new ElementMock((org.w3c.dom.Element) document.getElementsByTagName(
            "head").item(0)));
  }

  static BodyMock body;
  static HeadMock head;
  static Element html;

  public static BodyMock getBody() {
    return body;
  }

  public static HeadMock getHead() {
    return head;
  }

  public static Element getHtml() {
    return html;
  }

  private org.w3c.dom.Element e;

  public org.w3c.dom.Element getElement() {
    return e;
  }

  public ElementMock(String tag) {
    this.e = document.createElement(tag);
  }

  public ElementMock(org.w3c.dom.Element e) {
    if (!e.getOwnerDocument().equals(document)) {
      this.e = (org.w3c.dom.Element) document.importNode(e, true);
    } else {
      this.e = e;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    NamedNodeMap attributes = e.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Node attr = attributes.item(i);
      sb.append(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
    }
    String tagName = e.getTagName();
    return "<" + tagName + " " + sb.toString() + ">" + getInnerXml(e) + "</" + tagName + ">";
  }

  public static String innerXml(Node node) {
    DOMImplementationLS lsImpl =
        (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
    LSSerializer lsSerializer = lsImpl.createLSSerializer();
    NodeList childNodes = node.getChildNodes();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < childNodes.getLength(); i++) {
      String writeToString = lsSerializer.writeToString(childNodes.item(i));
      // Remove xml declaration
      String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>";
      if (writeToString.startsWith(xmlDeclaration)) {
        writeToString = writeToString.substring(xmlDeclaration.length() + 1);
      }
      sb.append(writeToString);
    }
    return sb.toString();
  }

  @Override
  public Element attr(String name, String value) {
    e.setAttribute(name, value);
    return this;
  }

  @Override
  public Element css(String property, String value) {
    assertCamelCase(property);

    String[] styles = attr("style").split(";");
    StringBuilder sb = new StringBuilder();
    boolean found = false;
    for (String s : styles) {
      if (s.isEmpty()) {
        continue;
      }
      String[] parts = s.split(":");
      if (parts.length != 2) {
        parts = new String[]{parts[0], ""};
      }
      if (property.equals(parts[0])) {
        parts[1] = value;
        found = true;
      }
      sb.append(parts[0] + ":" + parts[1] + ";");
    }
    if (!found) {
      sb.append(property + ":" + value + ";");
    }
    attr("style", sb.toString());

    return this;
  }

  @Override
  public Element html(String html) {
    setInnerXml(e, html);
    return this;
  }

  @Override
  public List<Element> children() {
    NodeList children = e.getChildNodes();
    ArrayList<Element> c = new ArrayList<Element>();
    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        c.add(new ElementMock((org.w3c.dom.Element) node));
      }
    }
    return c;
  }

  @Override
  public String attr(String name) {
    String value = e.getAttribute(name);
    return value == null ? "" : value;
  }

  @Override
  public String css(String property) {
    String[] styles = attr("style").split(";");
    for (String s : styles) {
      if (s.isEmpty()) {
        continue;
      }
      String[] parts = s.split(":");
      if (parts.length != 2) {
        parts = new String[]{parts[0], ""};
      }
      if (property.equals(parts[0])) {
        return parts[1];
      }
    }
    return "";
  }

  @Override
  public String html() {
    return getInnerXml(e);
  }

  @Override
  public String text() {
    return e.getTextContent();
  }

  @Override
  public Element text(String text) {
    e.setTextContent(text);
    return this;
  }

  @Override
  public String tag() {
    return e.getTagName();
  }

  @Override
  public List<Element> query(String selectors) {
    DOMNodeSelector nodeSelector = new DOMNodeSelector(e);
    Set<Node> children;
    try {
      children = nodeSelector.querySelectorAll(selectors);
    } catch (NodeSelectorException e1) {
      throw new RuntimeException(e1);
    }
    ArrayList<Element> c = new ArrayList<Element>();
    for (Node node : children) {
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        c.add(new ElementMock((org.w3c.dom.Element) node));
      }
    }
    return c;
  }

  @Override
  public Element addClassName(String className) {
    className = className.trim();

    // Get the current style string.
    String oldClassName = getClassName();
    int idx = oldClassName.indexOf(className);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || oldClassName.charAt(idx - 1) == ' ') {
        int last = idx + className.length();
        int lastPos = oldClassName.length();
        if ((last == lastPos) || ((last < lastPos) && (oldClassName.charAt(last) == ' '))) {
          break;
        }
      }
      idx = oldClassName.indexOf(className, idx + 1);
    }

    // Only add the style if it's not already present.
    if (idx == -1) {
      if (oldClassName.length() > 0) {
        oldClassName += " ";
      }
      setClassName(oldClassName + className);
    }

    return this;
  }

  @Override
  public Element removeClassName(String className) {
    className = className.trim();

    // Get the current style string.
    String oldStyle = getClassName();
    int idx = oldStyle.indexOf(className);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || oldStyle.charAt(idx - 1) == ' ') {
        int last = idx + className.length();
        int lastPos = oldStyle.length();
        if ((last == lastPos) || ((last < lastPos) && (oldStyle.charAt(last) == ' '))) {
          break;
        }
      }
      idx = oldStyle.indexOf(className, idx + 1);
    }

    // Don't try to remove the style if it's not there.
    if (idx != -1) {
      // Get the leading and trailing parts, without the removed name.
      String begin = oldStyle.substring(0, idx).trim();
      String end = oldStyle.substring(idx + className.length()).trim();

      // Some contortions to make sure we don't leave extra spaces.
      String newClassName;
      if (begin.length() == 0) {
        newClassName = end;
      } else if (end.length() == 0) {
        newClassName = begin;
      } else {
        newClassName = begin + " " + end;
      }

      setClassName(newClassName);
    }

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
    return attr("class");
  }

  @Override
  public boolean hasClassName(String className) {
    return getClassName().contains(className);
  }

  @Override
  public int absoluteBottom() {
    return 0;
  }

  @Override
  public int absoluteLeft() {
    return 0;
  }

  @Override
  public int absoluteRight() {
    return 0;
  }

  @Override
  public int absoluteTop() {
    return 0;
  }

  @Override
  public int clientHeight() {
    return 0;
  }

  @Override
  public int clientWidth() {
    return 0;
  }

  @Override
  public String dir() {
    return propertyString("dir");
  }

  @Override
  public List<Element> elementsByTagName(String name) {
    return query(name);
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
    return attr("id");
  }

  @Override
  public String lang() {
    return propertyString("lang");
  }

  @Override
  public int offsetHeight() {
    return 0;
  }

  @Override
  public int offsetLeft() {
    return 0;
  }

  @Override
  public Element parent() {
    Node parentNode = e.getParentNode();
    if (parentNode == null) {
      return null;
    }
    return new ElementMock((org.w3c.dom.Element) parentNode);
  }

  @Override
  public int offsetTop() {
    return 0;
  }

  @Override
  public int offsetWidth() {
    return 0;
  }

  @Override
  public boolean propertyBoolean(String name) {
    return (Boolean) Boolean.TRUE.equals(properties.get(name.toLowerCase()));
  }

  @Override
  public double propertyDouble(String name) {
    return (Double) properties.get(name.toLowerCase());
  }

  @Override
  public int propertyInt(String name) {
    Object i = properties.get(name.toLowerCase());
    return (Integer) (i == null ? 0 : i);
  }

  @Override
  public java.lang.Object propertyObject(String name) {
    return properties.get(name.toLowerCase());
  }

  @Override
  public String propertyString(String name) {
    return (String) properties.get(name.toLowerCase());
  }

  @Override
  public int scrollHeight() {
    return 0;
  }

  @Override
  public int scrollLeft() {
    return propertyInt("scrollLeft");
  }

  @Override
  public int scrollTop() {
    return propertyInt("scrollTop");
  }

  @Override
  public int scrollWidth() {
    return propertyInt("scrollWidth");
  }

  @Override
  public int tabIndex() {
    return propertyInt("tabindex");
  }

  @Override
  public String title() {
    return attr("title");
  }

  @Override
  public boolean hasAttribute(String name) {
    return e.getAttributes().getNamedItem(name) != null;
  }

  @Override
  public boolean hasTagName(String tagName) {
    return tag().equals(tagName);
  }

  @Override
  public Element removeAttr(String name) {
    e.removeAttribute(name);
    return this;
  }

  @Override
  public Element replaceClassName(String oldClassName, String newClassName) {
    removeClassName(oldClassName);
    addClassName(newClassName);
    return this;
  }

  @Override
  public Element scrollIntoView() {
    return this;
  }

  @Override
  public Element setClassName(String className) {
    attr("class", className);
    return this;
  }

  @Override
  public Element dir(String dir) {
    propertyString("dir", dir);
    return this;
  }

  @Override
  public Element id(String id) {
    attr("id", id);
    return this;
  }

  @Override
  public Element lang(String lang) {
    propertyString("lang", lang);
    return this;
  }

  @Override
  public Element propertyBoolean(String name, boolean value) {
    propertyObject(name, value);
    return this;
  }

  @Override
  public Element propertyDouble(String name, double value) {
    propertyObject(name, value);
    return this;
  }

  @Override
  public Element propertyInt(String name, int value) {
    propertyObject(name, value);
    return this;
  }

  @Override
  public Element propertyObject(String name, java.lang.Object value) {
    properties.put(name.toLowerCase(), value);
    return this;
  }

  @Override
  public Element propertyString(String name, String value) {
    propertyObject(name, value);
    return this;
  }

  @Override
  public Element scrollLeft(int scrollLeft) {
    propertyInt("scrollLeft", scrollLeft);
    return this;
  }

  @Override
  public Element scrollTop(int scrollTop) {
    propertyInt("scrollTop", scrollTop);
    return this;
  }

  @Override
  public Element tabIndex(int tabIndex) {
    propertyInt("tabindex", tabIndex);
    return this;
  }

  @Override
  public Element title(String title) {
    attr("title", title);
    return this;
  }

  // This method is only for testing
  @Override
  public void scrollWidth(int scrollWidth) {
    propertyInt("scrollWidth", scrollWidth);
  }

  protected Integer attrInt(String name) {
    String attr = attr(name);
    if (!attr.isEmpty()) {
      return Integer.valueOf(attr);
    } else {
      return 0;
    }
  }

  @Override
  public void setTarget(Object target) {
    throw new RuntimeException();
  }

  @Override
  public Element insert(Element element, int index) {
    NodeList children = e.getChildNodes();

    if (children.getLength() == index) {
      add(element);
      return this;
    } else if (children.getLength() < index) {
      throw new RuntimeException("Invalid index");
    }

    int pos = 0;
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (index == pos) {
          e.insertBefore(((ElementMock) element).e, n);
          break;
        }

        pos++;
      }
    }
    return this;
  }

  @Override
  public Element add(Element element) {
    e.appendChild(((ElementMock) element).e);
    return this;
  }

  @Override
  public Element remove(Element element) {
    remove(children().indexOf(element));
    return this;
  }

  @Override
  public Element css(String property, String value, Unit unit) {
    return css(property, value + unit);
  }

  @Override
  public Element remove(int index) {
    NodeList children = e.getChildNodes();

    int pos = 0;
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (index == pos) {
          e.removeChild(n);
          break;
        }

        pos++;
      }
    }
    return this;
  }

  @Override
  public Element removeFromParent() {
    Node parentNode = e.getParentNode();
    if (parentNode != null) {
      parentNode.removeChild(e);
    }
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Element> T from(Element el) {
    ElementMock e = (ElementMock) el;
    this.e = e.e;
    return (T) this;
  }

  @Override
  public Element insert(Element element, Element beforeElement) {
    insert(element, children().indexOf(beforeElement));
    return this;
  }

  public HandlerRegistration bindEvent(EventHandler handler, final String eventType) {
    handlers.put(eventType, handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        handlers.remove(eventType);
      }
    };
  }

  @Override
  public HandlerRegistration click(EventHandler handler) {
    return bindEvent(handler, "click");
  }

  @Override
  public HandlerRegistration dblclick(EventHandler handler) {
    return bindEvent(handler, "dblclick");
  }

  @Override
  public HandlerRegistration blur(EventHandler handler) {
    return bindEvent(handler, "blur");
  }

  @Override
  public HandlerRegistration focus(EventHandler handler) {
    return bindEvent(handler, "focus");
  }

  @Override
  public HandlerRegistration change(EventHandler handler) {
    return bindEvent(handler, "change");
  }

  @Override
  public HandlerRegistration mousedown(EventHandler handler) {
    return bindEvent(handler, "mousedown");
  }

  @Override
  public HandlerRegistration mouseup(EventHandler handler) {
    return bindEvent(handler, "mouseup");
  }

  @Override
  public HandlerRegistration mousemove(EventHandler handler) {
    return bindEvent(handler, "mousemove");
  }

  @Override
  public HandlerRegistration mouseout(EventHandler handler) {
    return bindEvent(handler, "mouseout");
  }

  @Override
  public HandlerRegistration mouseover(EventHandler handler) {
    return bindEvent(handler, "mouseover");
  }

  @Override
  public HandlerRegistration keydown(EventHandler handler) {
    return bindEvent(handler, "keydown");
  }

  @Override
  public HandlerRegistration keyup(EventHandler handler) {
    return bindEvent(handler, "keyup");
  }

  @Override
  public HandlerRegistration keypress(EventHandler handler) {
    return bindEvent(handler, "keypress");
  }

  @Override
  public HandlerRegistration load(EventHandler handler) {
    return bindEvent(handler, "load");
  }

  private void fireEvent(String eventType) {
    Event event = EventMock.get(eventType);
    for (EventHandler h : previewHandlers) {
      h.onEvent(event);
    }
    EventHandler eventHandler = handlers.get(eventType);
    if (eventHandler != null) {
      eventHandler.onEvent(event);
    }
  }

  @Override
  public void click() {
    fireEvent("click");
  }

  @Override
  public void dblclick() {
    fireEvent("dblclick");
  }

  @Override
  public void blur() {
    fireEvent("blur");
  }

  @Override
  public void focus() {
    fireEvent("focus");
  }

  @Override
  public void change() {
    fireEvent("change");
  }

  @Override
  public void mousedown() {
    fireEvent("mousedown");
  }

  @Override
  public void mouseup() {
    fireEvent("mouseup");
  }

  @Override
  public void mousemove() {
    fireEvent("mousemove");
  }

  @Override
  public void mouseout() {
    fireEvent("mouseout");
  }

  @Override
  public void mouseover() {
    fireEvent("mouseover");
  }

  @Override
  public void keydown() {
    fireEvent("keydown");
  }

  @Override
  public void keyup() {
    fireEvent("keyup");
  }

  @Override
  public void keypress() {
    fireEvent("keypress");
  }

  public static HandlerRegistration addNativePreviewHandler(final EventHandler handler) {
    previewHandlers.add(handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        previewHandlers.remove(handler);
      }
    };
  }

  @Override
  public Element nextSibling() {
    Node nextSibling = e.getNextSibling();
    if (nextSibling == null) {
      return null;
    }
    return new ElementMock((org.w3c.dom.Element) nextSibling);
  }

  @Override
  public Element previousSibling() {
    Node previousSibling = e.getPreviousSibling();
    if (previousSibling == null) {
      return null;
    }
    return new ElementMock((org.w3c.dom.Element) previousSibling);
  }

  // XML UTILS
  public static List<Element> getChildrenByTagName(org.w3c.dom.Element parent, String name) {
    return getChildrenByTagName(parent, new String[]{name});
  }

  public static List<Element> getChildrenByTagName(org.w3c.dom.Element parent, String[] names) {
    List<Element> nodeList = new ArrayList<Element>();
    for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        for (String name : names) {
          if (name.equals(child.getNodeName())) {
            nodeList.add((Element) child);
            break;
          }
        }
      }
    }
    return nodeList;
  }

  public static Element getFirstChildByTagName(org.w3c.dom.Element parent, String name) {
    List<Element> list = getChildrenByTagName(parent, new String[]{name});
    if (list.size() > 0)
      return list.get(0);
    else
      return null;
  }

  public static String getInnerXml(Node node) {
    try {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < node.getChildNodes().getLength(); i++)
        sb.append(getOuterXml(node.getChildNodes().item(i)));
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getOuterXml(Node n) throws ParserConfigurationException,
      TransformerFactoryConfigurationError, TransformerException {
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    // initialize StreamResult with File object to save to file
    StreamResult result = new StreamResult(new StringWriter());
    DOMSource source = new DOMSource(n);
    xformer.transform(source, result);
    String xmlString = result.getWriter().toString();
    return xmlString;
  }

  public static DocumentFragment parseFragment(Document doc, String xml) throws SAXException,
      IOException, ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentFragment fragment = doc.createDocumentFragment();
    String wellformedxml = "<__parseFragment__>" + xml + "</__parseFragment__>";
    Document fragmentdoc =
        dbf.newDocumentBuilder().parse(new InputSource(new StringReader(wellformedxml)));

    Node node = doc.importNode(fragmentdoc.getDocumentElement(), true);

    while (node.hasChildNodes()) {
      fragment.appendChild(node.removeChild(node.getFirstChild()));
    }

    return fragment;
  }

  public static void removeAllChildren(Node node) {
    while (node.hasChildNodes()) {
      node.removeChild(node.getFirstChild());
    }
  }

  public static void setInnerXml(Node node, String xml) {
    try {
      DocumentFragment fragment;
      fragment = parseFragment(node.getOwnerDocument(), xml);
      removeAllChildren(node);
      node.appendChild(fragment);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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
    clearProperty("float");
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
    clearProperty("opacity");
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
    css("float", value.getCssName());
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
    css("opacity", Double.toString(value));
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
    if (name.contains("-")) {
      throw new RuntimeException("The style name '" + name + "' should be in camelCase format");
    }
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
    if (getClass() != obj.getClass())
      return false;
    ElementMock other = (ElementMock) obj;
    if (e == null) {
      if (other.e != null)
        return false;
    } else if (!e.equals(other.e))
      return false;
    return true;
  }

  public static native boolean isVisible(Element elem) /*-{
		return (elem.style.display != 'none');
  }-*/;

  public static native void setVisible(Element elem, boolean visible) /*-{
		elem.style.display = visible ? '' : 'none';
  }-*/;

  @Override
  public void visible(boolean visible) {
    css("display", visible ? "" : "none");
  }

  @Override
  public boolean visible() {
    return !css("display").equals("none");
  }

  @Override
  public Element get(int index) {
    if (count() <= index) {
      throw new IndexOutOfBoundsException();
    }
    return new ElementMock((org.w3c.dom.Element) e.getChildNodes().item(index));
  }

  @Override
  public int count() {
    return e.getChildNodes().getLength();
  }

  public static Document getDocument() {
    return document;
  }

  public static Element getElementById(String id) {
    return new ElementMock(document.getElementById(id));
  }
}
