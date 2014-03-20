package com.guit.server.guice;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;
import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;
import com.google.inject.Inject;

import com.guit.client.command.CommandRpc;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.CommandException;
import com.guit.server.command.CommandRpcImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class RpcProcessor implements SerializationPolicyProvider {

  private final Method execute;
  private Method executeBatch;

  @Inject
  @StaticFilesPath
  String staticFilesPath;

  public SerializationPolicy serializationPolicyCache;

  @Inject
  public RpcProcessor() {
    try {
      this.execute = CommandRpcImpl.class.getMethod("execute", Action.class);
      this.executeBatch = CommandRpcImpl.class.getMethod("executeBatch", ArrayList.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String encodeResponse(Object data) {
    if (serializationPolicyCache == null) {
      throw new RuntimeException("Cannot push on the first request");
    }
    try {
      return (data instanceof Exception ? "//EX" : "//OK")
          + encodeResponse(data.getClass(), data, 1, serializationPolicyCache);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public String processRequest(String encodedRequest, CommandRpc service) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    Method method = execute;
    try {
      ServerSerializationStreamReader streamReader =
          new ServerSerializationStreamReader(classLoader, this);
      streamReader.prepareToRead(encodedRequest);

      serializationPolicyCache = streamReader.getSerializationPolicy();

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
        return "//OK"
            + encodeResponse(method.getReturnType(), method.invoke(service, parameterValues),
                flags, serializationPolicyCache);
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (!(cause instanceof CommandException)) {
          throw new UnexpectedException("Service method threw an unexpected exception: "
              + cause.toString(), cause);
        }

        return "//EX" + encodeResponse(cause.getClass(), cause, flags, serializationPolicyCache);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  protected String encodeResponse(Class<?> responseClass, Object object, int flags,
      SerializationPolicy serializationPolicy) throws SerializationException {

    ServerSerializationStreamWriter stream =
        new ServerSerializationStreamWriter(serializationPolicy);
    stream.setFlags(flags);

    stream.prepareToWrite();
    stream.serializeValue(object, responseClass);

    return stream.toString();
  }

  public final SerializationPolicy getSerializationPolicy(String moduleBaseURL, String strongName) {
    if (serializationPolicyCache != null) {
      return serializationPolicyCache;
    }

    serializationPolicyCache =
        loadSerializationPolicy(this, moduleBaseURL, strongName, staticFilesPath);

    if (serializationPolicyCache == null) {
      // Failed to get the requested serialization policy; use the default
      System.out
          .println("WARNING: Failed to get the SerializationPolicy '"
              + strongName
              + "' for module '"
              + moduleBaseURL
              + "'; a legacy, 1.3.3 compatible, serialization policy will be used.  You may experience SerializationExceptions as a result.");
      serializationPolicyCache = RPC.getDefaultSerializationPolicy();
    }

    return serializationPolicyCache;
  }

  static SerializationPolicy loadSerializationPolicy(RpcProcessor servlet, String moduleBaseURL,
      String strongName, String staticFilesPath) {
    // The request can tell you the path of the web app relative to the
    // container root.
    String contextPath = ""; // request.getContextPath();

    String modulePath = null;
    if (moduleBaseURL != null) {
      try {
        modulePath = new URL(moduleBaseURL).getPath();
      } catch (MalformedURLException ex) {
        // log the information, we will default
        servlet.log("Malformed moduleBaseURL: " + moduleBaseURL, ex);
      }
    }

    SerializationPolicy serializationPolicy = null;

    /*
     * Check that the module path must be in the same web app as the servlet itself. If you need to
     * implement a scheme different than this, override this method.
     */
    if (modulePath == null || !modulePath.startsWith(contextPath)) {
      String message =
          "ERROR: The module path requested, "
              + modulePath
              + ", is not in the same web application as this servlet, "
              + contextPath
              + ".  Your module may not be properly configured or your client and server code maybe out of date.";
      servlet.log(message);
    } else {
      // Strip off the context path from the module base URL. It should be a
      // strict prefix.
      String contextRelativePath = modulePath.substring(contextPath.length());

      String serializationPolicyFilePath =
          SerializationPolicyLoader
              .getSerializationPolicyFileName(contextRelativePath + strongName);

      // Open the RPC resource file and read its contents.
      InputStream is = null;
      try {
        is = new FileInputStream(new File(staticFilesPath + serializationPolicyFilePath));
      } catch (FileNotFoundException e1) {
      }
      try {
        if (is != null) {
          try {
            serializationPolicy = SerializationPolicyLoader.loadFromStream(is, null);
          } catch (ParseException e) {
            servlet.log("ERROR: Failed to parse the policy file '" + serializationPolicyFilePath
                + "'", e);
          } catch (IOException e) {
            servlet.log("ERROR: Could not read the policy file '" + serializationPolicyFilePath
                + "'", e);
          }
        } else {
          String message =
              "ERROR: The serialization policy file '" + serializationPolicyFilePath
                  + "' was not found; did you forget to include it in this deployment?";
          servlet.log(message);
        }
      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {
            // Ignore this error
          }
        }
      }
    }

    return serializationPolicy;
  }

  private void log(String message) {
    System.out.println(message);
  }

  private void log(String log, Throwable e) {
    System.out.println(log);
  }
}
