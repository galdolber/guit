package com.guit.junit.dom;

import com.guit.client.dom.Element;
import com.guit.client.dom.Form;
import com.guit.client.dom.Input;
import com.guit.client.dom.Textarea;

import java.util.ArrayList;
import java.util.List;

public class FormMock extends ElementMock implements Form {

  public FormMock() {
    super("form");
  }

  @Override
  public String acceptCharset() {
    return attr("acceptCharset");
  }

  @Override
  public String action() {
    return attr("action");
  }

  @Override
  public ArrayList<Element> elements() {
    ArrayList<Element> elements = new ArrayList<Element>();
    elements.addAll(query("input"));
    elements.addAll(query("textarea"));
    return elements;
  }

  @Override
  public String enctype() {
    return attr("enctype");
  }

  @Override
  public String method() {
    return attr("method");
  }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public String target() {
    return attr("target");
  }

  @Override
  public void reset() {
  }

  @Override
  public void acceptCharset(String acceptCharset) {
    attr("acceptCharset", acceptCharset);
  }

  @Override
  public void action(String action) {
    attr("action", action);
  }

  @Override
  public void enctype(String enctype) {
    attr("enctype", enctype);
  }

  @Override
  public void method(String method) {
    attr("method", method);
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }

  @Override
  public void target(String target) {
    attr("target", target);
  }

  @Override
  public void submit() {
  }

  @Override
  public void setFieldValue(String name, String value) {
    Element el = getField(name);
    if (el.tag().equals("input")) {
      Input input = new InputMock().from(el);
      if (el.attr("type").equals("checkbox")) {
        input.checked(value.equals("true") ? true : false);
      } else {
        input.text(value);
      }
    } else if (el.tag().equals("textarea")) {
      Textarea textarea = new TextareaMock().from(el);
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
      Input input = new InputMock().from(el);
      if (el.attr("type").equals("checkbox")) {
        return input.checked() ? "true" : "false";
      } else {
        return input.text();
      }
    } else if (el.tag().equals("textarea")) {
      Textarea textarea = new TextareaMock().from(el);
      return textarea.value();
    } else {
      throw new RuntimeException("Field type " + el.attr("type") + " is not supported");
    }
  }
}
