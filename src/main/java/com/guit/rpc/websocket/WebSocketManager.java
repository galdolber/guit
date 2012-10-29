package com.guit.rpc.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import com.guit.client.GuitEntryPoint;
import com.guit.rpc.RemoteServiceUrl;

import java.util.List;

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
      timerDelay = 1000;
      reconnectTimer.cancel();
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionOpenEvent());
    }
  };
  private CloseHandler closeHandler = new CloseHandler() {
    @Override
    public void onClose(WebSocket webSocket) {
      reconnectTimer.cancel();
      reconnectTimer.schedule(timerDelay);
      GuitEntryPoint.getEventBus().fireEvent(new ConnectionCloseEvent());
    }
  };

  private ErrorHandler errorHandler;
  private MessageHandler messageHandler;

  public WebSocketManager() {
    connect();
    INSTANCE = this;
  }

  public void connect() {
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

  static int index = 0;

  private static String getBaseUrl() {
    List<String> remoteUrl = url.getRemoteUrl();
    if (remoteUrl == null) {
      String s = GWT.getHostPageBaseURL();
      if (s.startsWith("http://")) {
        s = s.substring(7);
      } else if (s.startsWith("https://")) {
        s = s.substring(8);
      }
      return s;
    } else {
      if (index > remoteUrl.size() - 1) {
        index = 0;
      }
      String ret = remoteUrl.get(index);
      index++;
      return ret + "/";
    }
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
      throw new RuntimeException("The connection is not opened. Listen for ConnectionOpen event");
    }
  }
}
