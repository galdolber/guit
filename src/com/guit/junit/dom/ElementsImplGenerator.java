package com.guit.junit.dom;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ElementsImplGenerator {

  public static void main(String[] args) {
    File f = new File("src/com/guit/client/dom");
    String[] files = f.list();
    for (String file : files) {
      if (file.endsWith(".java")) {
        String className = file.substring(0, file.length() - 5);
        String canonicalName = "com.guit.client.dom." + className;
        Class<?> clazz = forName(canonicalName);
        if (className.equals("Element")) {
          continue;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("package com.guit.client.dom.impl;\n");
        sb.append("\n");
        sb.append("public class " + className + "Impl extends ElementImpl implements "
            + canonicalName + " {\n");

        sb.append("  \n");
        sb.append("  public " + className + "Impl() {\n");
        sb.append("    super(\"" + className.toLowerCase() + "\");\n");
        sb.append("  }\n");

        sb.append("  \n");
        sb.append("  private com.google.gwt.dom.client." + className + "Element el() {\n");
        sb.append("    return e.cast();\n");
        sb.append("  }\n");

        List<Method> methods = new ArrayList<Method>();
        getMethods(clazz, methods);
        for (Method m : methods) {
          Class<?>[] parameters = m.getParameterTypes();
          StringBuilder params = new StringBuilder();
          StringBuilder paramsCall = new StringBuilder();
          int n = 0;
          for (Class<?> p : parameters) {
            if (paramsCall.length() > 0) {
              paramsCall.append(", ");
              params.append(", ");
            }
            params.append(p.getName() + " arg" + n);
            paramsCall.append("arg" + n);
            n++;
          }

          String returnTypeString = print(m);
          boolean returnsVoid = returnTypeString.equals("void");
          String name = m.getName();
          String caller = name;
          if (!returnsVoid && !caller.startsWith("get")) {
            caller = "get" + caller.substring(0, 1).toUpperCase() + caller.substring(1);
          } else if (returnsVoid && !caller.startsWith("set")) {
            caller = "set" + caller.substring(0, 1).toUpperCase() + caller.substring(1);
          }

          sb.append("  \n");
          sb.append("  @Override\n");
          sb.append("  public " + returnTypeString + " " + name + "(" + params + ") {\n");
          sb.append("    " + (returnsVoid ? "" : "return ") + "el()." + caller + "(" + paramsCall
              + ");\n");
          sb.append("  }\n");
        }
        sb.append("}\n");
        try {
          Files.write(sb.toString().getBytes(), new File("src/com/guit/client/dom/impl/"
              + className + "Impl.java"));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static String print(Method m) {
    StringBuilder sb = new StringBuilder();

    Type c = m.getGenericReturnType();
    if (c instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) c;
      sb.append(((Class<?>) pt.getRawType()).getCanonicalName());
      sb.append("<");
      boolean f = true;
      for (Type t : pt.getActualTypeArguments()) {
        if (f) {
          f = false;
        } else {
          sb.append(", ");
        }
        sb.append(((Class<?>) t).getCanonicalName());
      }
      sb.append(">");
    } else {
      sb.append(m.getReturnType().getCanonicalName());
    }
    return sb.toString();
  }

  private static void getMethods(Class<?> clazz, List<Method> methods) {
    for (Method method : clazz.getDeclaredMethods()) {
      methods.add(method);
    }
    Class<?>[] interfaces = clazz.getInterfaces();
    for (Class<?> c : interfaces) {
      if (!c.equals(com.guit.client.dom.Element.class)) {
        getMethods(c, methods);
      }
    }
  }

  private static Class<?> forName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
