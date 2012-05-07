/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

import com.guit.client.GuitEntryPoint;
import com.guit.client.async.event.AsyncExceptionEvent;
import com.guit.rpc.websocket.CloseHandler;
import com.guit.rpc.websocket.ErrorHandler;
import com.guit.rpc.websocket.MessageEvent;
import com.guit.rpc.websocket.MessageHandler;
import com.guit.rpc.websocket.OpenHandler;
import com.guit.rpc.websocket.WebSocket;
import com.guit.rpc.websocket.WebSocketsException;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class WebSocketRemoteServiceProxy extends RemoteServiceProxy {

  private static WebSocket webSocket = WebSocket.create("ws://" + getBaseUrl() + "websocket");

  private static OpenHandler openHandler = new OpenHandler() {
    @Override
    public void onOpen(WebSocket webSocket) {
      GWT.log("Web socket connected");

      for (Entry<String, RequestCallbackAdapter<?>> e : beforeConnected.entrySet()) {
        sendRequest(e.getKey(), e.getValue());
      }
    }
  };
  private static CloseHandler closeHandler = new CloseHandler() {
    @Override
    public void onClose(WebSocket webSocket) {
      GWT.log("Web socket closed");
    }
  };
  private static MessageHandler messageHandler = new MessageHandler() {
    @Override
    public void onMessage(WebSocket webSocket, MessageEvent event) {
      String data = event.getData();
      GWT.log("Message " + data);
      if (data.startsWith("RPC")) {
        data = data.substring(3);
        int index = data.indexOf("_");
        Long requestNumber = Long.valueOf(data.substring(0, index));
        data = data.substring(index + 1);
        requestCallbacks.get(requestNumber).onResponseReceived(null, new GuitRequest(data));
        requestCallbacks.remove(requestNumber);
      }
    }
  };
  private static ErrorHandler errorHandler = new ErrorHandler() {
    @Override
    public void onError(WebSocket webSocket) {
      GWT.log("Error");
      GuitEntryPoint.getEventBus().fireEvent(new AsyncExceptionEvent(new WebSocketsException()));
    }
  };
  static {
    webSocket.setOnOpen(openHandler);
    webSocket.setOnClose(closeHandler);
    webSocket.setOnMessage(messageHandler);
    webSocket.setOnError(errorHandler);
  }

  /**
   * Name of the URL parameter that holds the RPC request payload.
   */
  public static final String URL_PARAM_REQUEST_PAYLOAD = "rpc";

  /**
   * Name of the URL parameter that holds the JSONP callback function name.
   */
  public static final String URL_PARAM_CALLBACK = "cb";

  /**
   * Name of the JSON object property that holds the RPC response payload.
   */
  public static final String JSON_PARAM_RESPONSE_PAYLOAD = "rpcResult";
  private static HashMap<Long, RequestCallbackAdapter<?>> requestCallbacks =
      new HashMap<Long, RequestCallbackAdapter<?>>();
  private static HashMap<String, RequestCallbackAdapter<?>> beforeConnected =
      new HashMap<String, RequestCallbackAdapter<?>>();
  private static long requestNumber = 0;

  protected WebSocketRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath,
      String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
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

  /**
   * Performs a remote service method invocation using a {@code script} tag.
   */
  @Override
  protected <T> Request doInvoke(final ResponseReader responseReader, final String methodName,
      final RpcStatsContext statsContext, String requestData, final AsyncCallback<T> callback) {

    requestData = "RPC" + requestNumber + "_" + requestData;
    RequestCallbackAdapter<T> requestCallbackAdapter =
        new RequestCallbackAdapter<T>(this, methodName, statsContext, callback, responseReader);
    if (webSocket.getReadyState() == WebSocket.OPEN) {
      sendRequest(requestData, requestCallbackAdapter);
    } else {
      beforeConnected.put(requestData, requestCallbackAdapter);
    }

    return null;
  }

  private static <T> void sendRequest(String data, RequestCallbackAdapter<T> callback) {
    requestCallbacks.put(requestNumber, callback);
    requestNumber++;
    webSocket.send(data);
  }
}
