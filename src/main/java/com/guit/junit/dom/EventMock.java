package com.guit.junit.dom;

import com.guit.client.dom.Element;
import com.guit.client.dom.Event;
import com.guit.client.dom.Touch;

import java.util.List;

public class EventMock implements Event {

  private static EventMock currentEvent = new EventMock();

  public static Event get(String eventType) {
    currentEvent.setType(eventType);
    return currentEvent;
  }

  public static void set(EventMock event) {
    currentEvent = event;
  }

  private boolean altKey;
  private int button;
  private List<Touch> changedTouches;
  private int charCode;
  private int clientX;
  private int clientY;
  private boolean ctrlKey;
  private boolean shiftKey;
  private List<Touch> targetTouches;
  private List<Touch> touched;
  private String type;
  private Element relatedEventTarget;
  private double rotation;
  private double scale;
  private int screenX;
  private int screenY;
  private Element currentEventTarget;
  private Element eventTarget;
  private int keyCode;
  private boolean metaKey;
  private int mouseWheelVelocityY;

  @Override
  public boolean getAltKey() {
    return altKey;
  }

  @Override
  public int getButton() {
    return button;
  }

  @Override
  public List<Touch> getChangedTouches() {
    return changedTouches;
  }

  @Override
  public int getCharCode() {
    return charCode;
  }

  @Override
  public int getClientX() {
    return clientX;
  }

  @Override
  public int getClientY() {
    return clientY;
  }

  @Override
  public boolean getCtrlKey() {
    return ctrlKey;
  }

  @Override
  public Element getCurrentEventTarget() {
    return currentEventTarget;
  }

  @Override
  public Element getEventTarget() {
    return eventTarget;
  }

  @Override
  public int getKeyCode() {
    return keyCode;
  }

  @Override
  public boolean getMetaKey() {
    return metaKey;
  }

  @Override
  public int getMouseWheelVelocityY() {
    return mouseWheelVelocityY;
  }

  @Override
  public Element getRelatedEventTarget() {
    return relatedEventTarget;
  }

  @Override
  public double getRotation() {
    return rotation;
  }

  @Override
  public double getScale() {
    return scale;
  }

  @Override
  public int getScreenX() {
    return screenX;
  }

  @Override
  public int getScreenY() {
    return screenY;
  }

  @Override
  public boolean getShiftKey() {
    return shiftKey;
  }

  @Override
  public List<Touch> getTargetTouches() {
    return targetTouches;
  }

  @Override
  public List<Touch> getTouches() {
    return touched;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void preventDefault() {
  }

  @Override
  public void stopPropagation() {
  }

  public List<Touch> getTouched() {
    return touched;
  }

  public void setTouched(List<Touch> touched) {
    this.touched = touched;
  }

  public void setAltKey(boolean altKey) {
    this.altKey = altKey;
  }

  public void setButton(int button) {
    this.button = button;
  }

  public void setChangedTouches(List<Touch> changedTouches) {
    this.changedTouches = changedTouches;
  }

  public void setCharCode(int charCode) {
    this.charCode = charCode;
  }

  public void setClientX(int clientX) {
    this.clientX = clientX;
  }

  public void setClientY(int clientY) {
    this.clientY = clientY;
  }

  public void setCtrlKey(boolean ctrlKey) {
    this.ctrlKey = ctrlKey;
  }

  public void setShiftKey(boolean shiftKey) {
    this.shiftKey = shiftKey;
  }

  public void setTargetTouches(List<Touch> targetTouches) {
    this.targetTouches = targetTouches;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setRelatedEventTarget(Element relatedEventTarget) {
    this.relatedEventTarget = relatedEventTarget;
  }

  public void setRotation(double rotation) {
    this.rotation = rotation;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public void setScreenX(int screenX) {
    this.screenX = screenX;
  }

  public void setScreenY(int screenY) {
    this.screenY = screenY;
  }

  public void setCurrentEventTarget(Element currentEventTarget) {
    this.currentEventTarget = currentEventTarget;
  }

  public void setEventTarget(Element eventTarget) {
    this.eventTarget = eventTarget;
  }

  public void setKeyCode(int keyCode) {
    this.keyCode = keyCode;
  }

  public void setMetaKey(boolean metaKey) {
    this.metaKey = metaKey;
  }

  public void setMouseWheelVelocityY(int mouseWheelVelocityY) {
    this.mouseWheelVelocityY = mouseWheelVelocityY;
  }

  @Override
  public String toDebugString() {
    return toString();
  }
}
