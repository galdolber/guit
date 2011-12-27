package com.guit.client.dom.impl;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;

import com.guit.client.dom.Element;
import com.guit.client.dom.Event;
import com.guit.client.dom.Touch;

import java.util.ArrayList;
import java.util.List;

public class EventImpl implements Event {

  private NativeEvent e;

  public EventImpl(NativeEvent e) {
    this.e = e;
  }

  @Override
  public boolean getAltKey() {
    return e.getAltKey();
  }

  @Override
  public int getButton() {
    return e.getButton();
  }

  @Override
  public List<Touch> getChangedTouches() {
    return toList(e.getChangedTouches());
  }

  private List<Touch> toList(JsArray<com.google.gwt.dom.client.Touch> touches) {
    ArrayList<Touch> list = new ArrayList<Touch>();
    for (int i = 0; i < touches.length(); i++) {
      list.add(new TouchImpl(touches.get(i)));
    }
    return list;
  }

  @Override
  public int getCharCode() {
    return e.getCharCode();
  }

  @Override
  public int getClientX() {
    return e.getClientX();
  }

  @Override
  public int getClientY() {
    return e.getClientY();
  }

  @Override
  public boolean getCtrlKey() {
    return e.getCtrlKey();
  }

  @Override
  public Element getCurrentEventTarget() {
    com.google.gwt.dom.client.Element curr = e.getCurrentEventTarget().cast();
    return new ElementImpl(curr);
  }

  @Override
  public Element getEventTarget() {
    com.google.gwt.dom.client.Element curr = e.getEventTarget().cast();
    return new ElementImpl(curr);
  }

  @Override
  public int getKeyCode() {
    return e.getKeyCode();
  }

  @Override
  public boolean getMetaKey() {
    return e.getMetaKey();
  }

  @Override
  public int getMouseWheelVelocityY() {
    return e.getMouseWheelVelocityY();
  }

  @Override
  public Element getRelatedEventTarget() {
    com.google.gwt.dom.client.Element curr = e.getRelatedEventTarget().cast();
    return new ElementImpl(curr);
  }

  @Override
  public double getRotation() {
    return e.getRotation();
  }

  @Override
  public double getScale() {
    return e.getScale();
  }

  @Override
  public int getScreenX() {
    return e.getScreenX();
  }

  @Override
  public int getScreenY() {
    return e.getScreenY();
  }

  @Override
  public boolean getShiftKey() {
    return e.getShiftKey();
  }

  @Override
  public List<Touch> getTargetTouches() {
    return toList(e.getTargetTouches());
  }

  @Override
  public List<Touch> getTouches() {
    return toList(e.getTouches());
  }

  @Override
  public String getType() {
    return e.getType();
  }

  @Override
  public void preventDefault() {
    e.preventDefault();
  }

  @Override
  public void stopPropagation() {
    e.stopPropagation();
  }

  @Override
  public String toDebugString() {
    return e.toString();
  }

  public NativeEvent getNativeEvent() {
    return e;
  }
}
