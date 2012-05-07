package com.guit.rpc.websocket;

import com.guit.client.command.action.Action;
import com.guit.client.command.action.Response;

public class ServerPushData implements Action<Response> {

  private Pushable data;

  public ServerPushData() {
  }

  public ServerPushData(Pushable data) {
    this.setData(data);
  }

  public Pushable getData() {
    return data;
  }

  public void setData(Pushable data) {
    this.data = data;
  }
}
