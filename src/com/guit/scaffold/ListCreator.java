package com.guit.scaffold;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates the base a celltable for a pojo.
 */
public class ListCreator extends Creator {

  public void createJava(Class<?> pojoType, String pack, OutputStream output) throws IOException,
      TemplateException {
    // Print columns
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());

    // Jdo, jpa, objectify
    Method idGetter = getMethod(pojoType, "getId");
    if (idGetter == null) {
      // Slim3
      idGetter = getMethod(pojoType, "getKey");
      if (idGetter == null) {
        throw new RuntimeException("No id or key found on type '" + pojoName
            + "'. It should have a Long getId() or Key getKey() method");
      }

      root.put("keyPath", "getKey().getId()");
    } else {
      root.put("keyPath", "getId()");
    }

    Template temp = cfg.getTemplate("list.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public Method getMethod(Class<?> pojoType, String name) {
    try {
      return pojoType.getDeclaredMethod(name);
    } catch (Exception e) {
      return null;
    }
  }

  public void createXml(Class<?> pojoType, String pack, OutputStream output) throws IOException,
      TemplateException {
    // Print columns
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());

    Template temp = cfg.getTemplate("list.xml.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }
}
