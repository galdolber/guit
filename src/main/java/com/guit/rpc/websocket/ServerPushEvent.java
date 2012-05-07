package com.guit.rpc.websocket;

import com.google.gwt.event.shared.GwtEvent;

public class ServerPushEvent extends GwtEvent<ServerPushHandler> {

  private static Type<ServerPushHandler> type;

  public static Type<ServerPushHandler> getType() {
    return type != null ? type : (type = new Type<ServerPushHandler>());
  }

  private final Object data;

  public ServerPushEvent(Object data) {
    this.data = data;
  }

  @Override
  protected void dispatch(ServerPushHandler handler) {
    handler.onServerPush(this);
  }

  @Override
  public Type<ServerPushHandler> getAssociatedType() {
    return getType();
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    return "ServerPushEvent [data=" + data + "]";
  }
}
