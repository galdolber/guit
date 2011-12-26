/*
 * Copyright 2010 Gal Dolber.
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
package com.guit.server.guice;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;
import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.guit.client.command.action.Action;
import com.guit.client.command.action.CommandException;
import com.guit.server.command.CommandRpcImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class GuiceGwtServlet extends RemoteServiceServlet {

  @Inject
  CommandRpcImpl service;
  private final Method execute;
  private Method executeBatch;

  @Inject
  public GuiceGwtServlet(CommandRpcImpl service) {
    this.service = service;
    try {
      this.execute = CommandRpcImpl.class.getMethod("execute", Action.class);
      this.executeBatch = CommandRpcImpl.class.getMethod("executeBatch", ArrayList.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String processCall(String payload) {
    checkPermutationStrongName();

    return decodeRequest(payload, null, this);
  }

  public String decodeRequest(String encodedRequest, Class<?> type,
      SerializationPolicyProvider serializationPolicyProvider) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    Method method = execute;
    try {
      ServerSerializationStreamReader streamReader =
          new ServerSerializationStreamReader(classLoader, serializationPolicyProvider);
      streamReader.prepareToRead(encodedRequest);

      SerializationPolicy serializationPolicy = streamReader.getSerializationPolicy();

      // Predecible values
      streamReader.readString();

      // Method name
      String methodName = streamReader.readString();

      // Ignore, we know the values
      streamReader.readInt();
      streamReader.readString();

      Object[] parameterValues = new Object[1];
      if ("execute".equals(methodName)) {
        method = execute;
        parameterValues[0] = streamReader.deserializeValue(Action.class);
      } else {
        method = executeBatch;
        parameterValues[0] = streamReader.deserializeValue(ArrayList.class);
      }

      int flags = streamReader.getFlags();
      try {
        return encodeResponse(method.getReturnType(), method.invoke(service, parameterValues),
            false, flags, serializationPolicy);
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (!(cause instanceof CommandException)) {
          throw new UnexpectedException("Service method threw an unexpected exception: "
              + cause.toString(), cause);
        }

        return encodeResponse(cause.getClass(), cause, true, flags, serializationPolicy);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  protected String encodeResponse(Class<?> responseClass, Object object, boolean wasThrown,
      int flags, SerializationPolicy serializationPolicy) throws SerializationException {

    ServerSerializationStreamWriter stream =
        new ServerSerializationStreamWriter(serializationPolicy);
    stream.setFlags(flags);

    stream.prepareToWrite();
    stream.serializeValue(object, responseClass);

    return (wasThrown ? "//EX" : "//OK") + stream.toString();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      synchronized (this) {
        validateThreadLocalData();
        perThreadRequest.set(request);
        perThreadResponse.set(response);
      }

      processGet(request, response);

    } catch (Throwable e) {
      doUnexpectedFailure(e);
    } finally {
      perThreadRequest.set(null);
      perThreadResponse.set(null);
    }
  }

  public void processGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException, SerializationException {
    String requestPayload = readContent(request);
    onBeforeRequestDeserialized(requestPayload);
    // Direct call to avoid xs check
    String responsePayload = decodeRequest(requestPayload, null, this);
    onAfterResponseSerialized(responsePayload);
    String callback = request.getParameter("callback");
    if (callback != null) {
      responsePayload = callback + "(" + quote(responsePayload) + ");";
    }
    writeResponse(request, response, responsePayload);
  }

  private void validateThreadLocalData() {
    if (perThreadRequest == null) {
      perThreadRequest = new ThreadLocal<HttpServletRequest>();
    }
    if (perThreadResponse == null) {
      perThreadResponse = new ThreadLocal<HttpServletResponse>();
    }
  }

  protected void writeResponse(HttpServletRequest request, HttpServletResponse response,
      String responsePayload) throws IOException {
    boolean gzipEncode =
        RPCServletUtils.acceptsGzipEncoding(request)
            && shouldCompressResponse(request, response, responsePayload);

    RPCServletUtils.writeResponse(getServletContext(), response, responsePayload, gzipEncode);
  }

  @Override
  protected String readContent(HttpServletRequest request) throws ServletException, IOException {
    if (request.getMethod().equals("POST")) {
      return super.readContent(request);
    } else {
      return request.getParameter("a");
    }
  }

  /**
   * Produce a string in double quotes with backslash sequences in all the right
   * places. A backslash will be inserted within </, allowing JSON text to be
   * delivered in HTML. In JSON text, a string cannot contain a control
   * character or an unescaped quote or backslash.
   * 
   * @param string A String
   * @return A String correctly formatted for insertion in a JSON text.
   */
  public static String quote(String string) {
    if (string == null || string.length() == 0) {
      return "\"\"";
    }

    char b;
    char c = 0;
    int i;
    int len = string.length();
    StringBuffer sb = new StringBuffer(len + 4);
    String t;

    sb.append('"');
    for (i = 0; i < len; i += 1) {
      b = c;
      c = string.charAt(i);
      switch (c) {
        case '\\':
        case '"':
          sb.append('\\');
          sb.append(c);
          break;
        case '/':
          if (b == '<') {
            sb.append('\\');
          }
          sb.append(c);
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\r':
          sb.append("\\r");
          break;
        default:
          if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
            t = "000" + Integer.toHexString(c);
            sb.append("\\u" + t.substring(t.length() - 4));
          } else {
            sb.append(c);
          }
      }
    }
    sb.append('"');
    return sb.toString();
  }
}
