package com.guit.rpc.websocket;

import com.google.gwt.core.client.GWT;

import com.guit.client.GuitEntryPoint;
import com.guit.rpc.RemoteServiceUrl;

public class WebSocketManager {

  private static WebSocketManager INSTANCE;

  private static RemoteServiceUrl url = GWT.create(RemoteServiceUrl.class);

  public static WebSocketManager get() {
    return INSTANCE;
  }

  private WebSocket webSocket;

  private OpenHandler openHandler = new OpenHandler() {
    @Override
    public void onOpen(WebSocket webSocket) {
      if (queueData != null) {
        send(queueData);
        queueData = null;
      }
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionOpenEvent());
    }
  };
  private CloseHandler closeHandler = new CloseHandler() {
    @Override
    public void onClose(WebSocket webSocket) {
      connecting = false;
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionCloseEvent());
    }
  };

  private ErrorHandler errorHandler;
  private MessageHandler messageHandler;

  private String queueData = null;

  private boolean connecting;

  public WebSocketManager() {
    connect();
    INSTANCE = this;
  }

  public void connect() {
    if (connecting) {
      return;
    }
    connecting = true;
    webSocket = WebSocket.create(getBaseUrl() + "websocket");
    webSocket.setOnOpen(openHandler);
    webSocket.setOnClose(closeHandler);
    if (errorHandler != null) {
      webSocket.setOnError(errorHandler);
    }
    if (messageHandler != null) {
      webSocket.setOnMessage(messageHandler);
    }
  }

  private static String getBaseUrl() {
    String s = GWT.getHostPageBaseURL();
    boolean isSecure = false;
    if (s.startsWith("http://")) {
      s = s.substring(7);
    } else if (s.startsWith("https://")) {
      isSecure = true;
      s = s.substring(8);
    }
    
    if (s.endsWith("/")) {
      s = s.substring(0, s.length() - 1);
    }
    
    int index = s.indexOf(":");
    String sport = "";
    if (index != -1) {
      sport = s.substring(index + 1);
      s = s.substring(0, index);
    }
    
    Integer port = url.getPort();
    
    return (isSecure ? "wss://" : "ws://") + s + ":" + (port == null ? (sport.isEmpty() ? "80" : sport) : port.toString()) + "/";
  }

  public int getReadyState() {
    return webSocket.getReadyState();
  }

  public void setOnMessage(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
    webSocket.setOnMessage(messageHandler);
  }

  public void setOnError(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
    webSocket.setOnError(errorHandler);
  }

  public void send(String data) {
    if (webSocket.getReadyState() == WebSocket.OPEN) {
      webSocket.send(data);
    } else {
      connect();
      queueData = data;
    }
  }
}
