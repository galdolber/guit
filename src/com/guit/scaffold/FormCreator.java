package com.guit.scaffold;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates the base a celltable for a pojo.
 */
public class FormCreator extends Creator {

  public void createJava(Class<?> pojoType, String pack, OutputStream output) throws IOException,
      TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());

    for (Field f : pojoType.getDeclaredFields()) {
      String name = f.getName();
      if (name.equals("id") || name.equals("key")) {
        continue;
      }
      root.put("autofocus", name);
      break;
    }

    Template temp = cfg.getTemplate("form.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createXml(Class<?> pojoType, String pack, OutputStream output) throws IOException,
      TemplateException {
    String pojoName = pojoType.getSimpleName();

    ArrayList<FieldTemplate> fields = new ArrayList<FieldTemplate>();

    for (Field f : pojoType.getDeclaredFields()) {
      String name = f.getName();
      if (name.equals("id")) {
        continue;
      }
      String tag = "g:";
      Class<?> fieldType = f.getType();
      if (fieldType.equals(String.class)) {
        tag += TextBox.class.getSimpleName();
      } else if (fieldType.equals(Integer.class)) {
        tag += IntegerBox.class.getSimpleName();
      } else if (fieldType.equals(Long.class)) {
        tag += LongBox.class.getSimpleName();
      } else if (fieldType.equals(Double.class)) {
        tag += DoubleBox.class.getSimpleName();
      } else if (fieldType.equals(Boolean.class)) {
        tag += CheckBox.class.getSimpleName();
      } else if (fieldType.equals(Date.class)) {
        tag = "d:";
        tag += DateBox.class.getSimpleName();
      } else if (fieldType.isEnum()) {
        Guit.create(Command.enumeditor, fieldType);
        tag = "v:";
        tag += fieldType.getSimpleName() + "Editor";
      } else {
        System.out.println("FormCreator: the type " + fieldType.getCanonicalName()
            + " is not supported");
        continue;
      }
      fields.add(new FieldTemplate(name, tag, name.substring(0, 1).toUpperCase()
          + name.substring(1)));
    }

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("fields", fields);

    Template temp = cfg.getTemplate("form.xml.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }
}
