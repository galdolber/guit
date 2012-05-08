package com.guit.rpc.websocket;

import com.google.gwt.core.client.GWT;

import com.guit.client.GuitEntryPoint;

public class WebSocketManager {

  private static WebSocketManager INSTANCE;

  public static WebSocketManager get() {
    return INSTANCE;
  }

  private WebSocket webSocket = WebSocket.create("ws://" + getBaseUrl() + "websocket");

  private OpenHandler openHandler = new OpenHandler() {
    @Override
    public void onOpen(WebSocket webSocket) {
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionOpenEvent());
    }
  };
  private CloseHandler closeHandler = new CloseHandler() {
    @Override
    public void onClose(WebSocket webSocket) {
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionCloseEvent());
    }
  };

  public WebSocketManager() {
    webSocket.setOnOpen(openHandler);
    webSocket.setOnClose(closeHandler);
    INSTANCE = this;
  }

  private static String getBaseUrl() {
    String s = GWT.getHostPageBaseURL();
    if (s.startsWith("http://")) {
      s = s.substring(7);
    } else if (s.startsWith("https://")) {
      s = s.substring(8);
    }
    return s;
  }

  public int getReadyState() {
    return webSocket.getReadyState();
  }

  public void setOnMessage(MessageHandler messageHandler) {
    webSocket.setOnMessage(messageHandler);
  }

  public void setOnError(ErrorHandler errorHandler) {
    webSocket.setOnError(errorHandler);
  }

  public void send(String data) {
    webSocket.send(data);
  }
}
