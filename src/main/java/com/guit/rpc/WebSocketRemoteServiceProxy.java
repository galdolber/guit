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
import com.google.gwt.rpc.client.impl.RemoteException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

import com.guit.client.GuitEntryPoint;
import com.guit.client.async.event.AsyncExceptionEvent;
import com.guit.rpc.websocket.ErrorHandler;
import com.guit.rpc.websocket.MessageEvent;
import com.guit.rpc.websocket.MessageHandler;
import com.guit.rpc.websocket.ServerPushData;
import com.guit.rpc.websocket.ServerPushEvent;
import com.guit.rpc.websocket.WebSocket;
import com.guit.rpc.websocket.WebSocketManager;
import com.guit.rpc.websocket.WebSocketsException;

import java.util.HashMap;

public abstract class WebSocketRemoteServiceProxy extends RemoteServiceProxy {

  private WebSocketManager webSocketManager = WebSocketManager.get();

  private MessageHandler messageHandler = new MessageHandler() {
    @Override
    public void onMessage(WebSocket webSocket, MessageEvent event) {
      String data = event.getData();
      if (data.startsWith("RPC")) {
        data = data.substring(3);
        int index = data.indexOf("_");
        Long requestNumber = Long.valueOf(data.substring(0, index));
        data = data.substring(index + 1);
        requestCallbacks.get(requestNumber).onResponseReceived(null, new GuitRequest(data));
        requestCallbacks.remove(requestNumber);
      } else {
        try {
          GuitEntryPoint.getEventBus().fireEvent(
              new ServerPushEvent(((ServerPushData) createStreamReader(data).readObject())
                  .getData()));
        } catch (RemoteException e) {
          throw new RuntimeException(e.getCause());
        } catch (SerializationException e) {
          throw new IncompatibleRemoteServiceException("The response could not be deserialized", e);
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      }
    }
  };
  private ErrorHandler errorHandler = new ErrorHandler() {
    @Override
    public void onError(WebSocket webSocket) {
      GWT.log("WebSocket Exception");
      GuitEntryPoint.getEventBus().fireEvent(new AsyncExceptionEvent(new WebSocketsException()));
    }
  };

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
  private static long requestNumber = 0;

  protected WebSocketRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath,
      String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);

    webSocketManager.setOnMessage(messageHandler);
    webSocketManager.setOnError(errorHandler);
  }

  /**
   * Performs a remote service method invocation using a {@code script} tag.
   */
  @Override
  protected <T> Request doInvoke(final ResponseReader responseReader, final String methodName,
      final RpcStatsContext statsContext, String requestData, final AsyncCallback<T> callback) {

    requestData = requestNumber + "_" + requestData;
    RequestCallbackAdapter<T> requestCallbackAdapter =
        new RequestCallbackAdapter<T>(this, methodName, statsContext, callback, responseReader);
    if (webSocketManager.getReadyState() == WebSocket.OPEN) {
      sendRequest(requestData, requestCallbackAdapter);
    } else {
      throw new RuntimeException("The connection is not opened. Listen for ConnectionOpenEvent");
    }

    return null;
  }

  private <T> void sendRequest(String data, RequestCallbackAdapter<T> callback) {
    requestCallbacks.put(requestNumber, callback);
    requestNumber++;
    webSocketManager.send(data);
  }
}
