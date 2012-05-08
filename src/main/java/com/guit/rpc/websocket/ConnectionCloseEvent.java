package com.guit.rpc.websocket;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectionCloseEvent extends GwtEvent<ConnectionCloseHandler> {

  private static Type<ConnectionCloseHandler> type;

  public static Type<ConnectionCloseHandler> getType() {
    return type != null ? type : (type = new Type<ConnectionCloseHandler>());
  }

  public ConnectionCloseEvent() {
  }

  @Override
  protected void dispatch(ConnectionCloseHandler handler) {
    handler.onConnectionClose(this);
  }

  @Override
  public Type<ConnectionCloseHandler> getAssociatedType() {
    return getType();
  }

  @Override
  public String toString() {
    return "ConnectionCloseEvent";
  }
}
