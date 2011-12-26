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
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

public abstract class XsRemoteServiceProxy extends RemoteServiceProxy {
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

  protected XsRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath,
      String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
  }

  /**
   * Performs a remote service method invocation using a {@code script} tag.
   */
  @Override
  protected <T> Request doInvoke(final ResponseReader responseReader, final String methodName,
      final RpcStatsContext statsContext, final String requestData, final AsyncCallback<T> callback) {

    final RequestCallbackAdapter<T> requestCallback =
        new RequestCallbackAdapter<T>(this, methodName, statsContext, callback, responseReader);

    JsonpRequestBuilder builder = new JsonpRequestBuilder();
    builder.requestString(getServiceEntryPoint() + "?a=" + URL.encode(requestData),
        new AsyncCallback<String>() {
          @Override
          public void onSuccess(String result) {
            requestCallback.onResponseReceived(null, new GuitRequest(result));
          }

          @Override
          public void onFailure(Throwable caught) {
            requestCallback.onError(null, caught);
          }
        });

    return null;
  }
}
