package com.guit.rpc.websocket;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectionOpenEvent extends GwtEvent<ConnectionOpenHandler> {

  private static Type<ConnectionOpenHandler> type;

  public static Type<ConnectionOpenHandler> getType() {
    return type != null ? type : (type = new Type<ConnectionOpenHandler>());
  }

  public ConnectionOpenEvent() {
  }

  @Override
  protected void dispatch(ConnectionOpenHandler handler) {
    handler.onConnectionOpen(this);
  }

  @Override
  public Type<ConnectionOpenHandler> getAssociatedType() {
    return getType();
  }

  @Override
  public String toString() {
    return "ConnectionOpenEvent";
  }
}
