package com.guit.rpc.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import com.guit.client.GuitEntryPoint;
import com.guit.rpc.RemoteServiceUrl;

public class WebSocketManager {

  private static WebSocketManager INSTANCE;

  private static RemoteServiceUrl url = GWT.create(RemoteServiceUrl.class);

  public static WebSocketManager get() {
    return INSTANCE;
  }

  private WebSocket webSocket;

  private Timer reconnectTimer = new Timer() {
    @Override
    public void run() {
      connect();
      timerDelay += 1000;
    }
  };

  private int timerDelay = 1000;

  private OpenHandler openHandler = new OpenHandler() {
    @Override
    public void onOpen(WebSocket webSocket) {
      if (queueData != null) {
        send(queueData);
        queueData = null;
      }
      timerDelay = 1000;
      reconnectTimer.cancel();
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionOpenEvent());
    }
  };
  private CloseHandler closeHandler = new CloseHandler() {
    @Override
    public void onClose(WebSocket webSocket) {
      connecting = false;
      reconnectTimer.cancel();
      reconnectTimer.schedule(timerDelay);
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
    webSocket = WebSocket.create("ws://" + getBaseUrl() + "websocket");
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
    Integer port = url.getPort();
    String s = GWT.getHostPageBaseURL();
    if (s.startsWith("http://")) {
      s = s.substring(7);
    } else if (s.startsWith("https://")) {
      s = s.substring(8);
    }
    
    int index = s.indexOf(":");
    String sport = "";
    if (index != -1) {
      sport = s.substring(index + 1);
      s = s.substring(0, index);
    }
    
    return s + ":" + (port == null ? (sport.isEmpty() ? "80" : sport) : port.toString()) + "/";
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
