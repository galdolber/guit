package com.guit.client.dom.impl;

import com.guit.client.dom.Element;
import com.guit.client.dom.Input;
import com.guit.client.dom.Textarea;

import java.util.List;

public class FormImpl extends ElementImpl implements com.guit.client.dom.Form {

  public FormImpl() {
    super("form");
  }

  private com.google.gwt.dom.client.FormElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String name() {
    return el().getName();
  }

  @Override
  public java.lang.String method() {
    return el().getMethod();
  }

  @Override
  public void name(java.lang.String arg0) {
    el().setName(arg0);
  }

  @Override
  public void reset() {
    el().reset();
  }

  @Override
  public void method(java.lang.String arg0) {
    el().setMethod(arg0);
  }

  @Override
  public java.util.ArrayList<com.guit.client.dom.Element> elements() {
    return nodesToElements(el().getElements());
  }

  @Override
  public java.lang.String target() {
    return el().getTarget();
  }

  @Override
  public void target(java.lang.String arg0) {
    el().setTarget(arg0);
  }

  @Override
  public java.lang.String acceptCharset() {
    return el().getAcceptCharset();
  }

  @Override
  public java.lang.String action() {
    return el().getAction();
  }

  @Override
  public java.lang.String enctype() {
    return el().getEnctype();
  }

  @Override
  public void acceptCharset(java.lang.String arg0) {
    el().setAcceptCharset(arg0);
  }

  @Override
  public void action(java.lang.String arg0) {
    el().setAction(arg0);
  }

  @Override
  public void enctype(java.lang.String arg0) {
    el().setEnctype(arg0);
  }

  @Override
  public void submit() {
    el().submit();
  }

  @Override
  public void setFieldValue(String name, String value) {
    Element el = getField(name);
    if (el.tag().equals("input")) {
      Input input = new InputImpl().from(el);
      if (el.attr("type").equals("checkbox")) {
        input.checked(value.equals("true") ? true : false);
      } else {
        input.text(value);
      }
    } else if (el.tag().equals("textarea")) {
      Textarea textarea = new TextareaImpl().from(el);
      textarea.value(value);
    } else {
      throw new RuntimeException("Field type " + el.attr("type") + " is not supported");
    }
  }

  @Override
  public Element getField(String name) {
    List<Element> query = query("input[name='" + name + "']");
    if (query.size() == 0) {
      query = query("textarea[name='" + name + "']");
      if (query.size() == 0) {
        throw new RuntimeException("Field does not exists");
      }
    }
    return query.get(0);
  }

  @Override
  public String getFieldValue(String name) {
    Element el = getField(name);
    if (el.tag().equals("input")) {
      Input input = new InputImpl().from(el);
      if (el.attr("type").equals("checkbox")) {
        return input.checked() ? "true" : "false";
      } else {
        return input.text();
      }
    } else if (el.tag().equals("textarea")) {
      Textarea textarea = new TextareaImpl().from(el);
      return textarea.value();
    } else {
      throw new RuntimeException("Field type " + el.attr("type") + " is not supported");
    }
  }
}
