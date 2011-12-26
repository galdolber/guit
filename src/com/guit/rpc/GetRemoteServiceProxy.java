/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

public abstract class GetRemoteServiceProxy extends RemoteServiceProxy {
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

  protected GetRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath,
      String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
  }

  /**
   * Performs a remote service method invocation using a {@code script} tag.
   */
  @Override
  protected <T> Request doInvoke(final ResponseReader responseReader, final String methodName,
      final RpcStatsContext statsContext, final String requestData, final AsyncCallback<T> callback) {

    RequestBuilder b =
        new RequestBuilder(RequestBuilder.GET, getServiceEntryPoint() + "?a="
            + URL.encode(requestData));

    final RequestCallbackAdapter<T> requestCallback =
        new RequestCallbackAdapter<T>(this, methodName, statsContext, callback, responseReader);

    try {
      b.sendRequest(null, new RequestCallback() {
        @Override
        public void onResponseReceived(Request request, Response response) {
          requestCallback.onResponseReceived(null, new GuitRequest(response.getText()));
        }

        @Override
        public void onError(Request request, Throwable exception) {
          requestCallback.onError(null, exception);
        }
      });
    } catch (RequestException e) {
      requestCallback.onError(null, e.getCause());
    }

    return null;
  }
}
