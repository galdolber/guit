package com.guit.rpc.websocket;

import com.google.gwt.event.shared.GwtEvent;

public class ForcedRefreshEvent extends GwtEvent<ForcedRefreshHandler> {

  private static Type<ForcedRefreshHandler> type;

  public static Type<ForcedRefreshHandler> getType() {
    return type != null ? type : (type = new Type<ForcedRefreshHandler>());
  }

  public ForcedRefreshEvent() {
  }

  @Override
  protected void dispatch(ForcedRefreshHandler handler) {
    handler.onForcedRefresh(this);
  }

  @Override
  public Type<ForcedRefreshHandler> getAssociatedType() {
    return getType();
  }

  @Override
  public String toString() {
    return "ConnectionOpenEvent";
  }
}
