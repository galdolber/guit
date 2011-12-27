package com.guit.scaffold;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates the base a celltable for a pojo.
 */
public class ServiceCreator extends Creator {

  public void createService(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("service.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createServiceImpl(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("serviceimpl.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createServiceImplJpa(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("serviceimpljpa.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createServiceImplJdo(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("serviceimpljdo.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createPMF(String pack, OutputStream output) throws IOException, TemplateException {
    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    Template temp = cfg.getTemplate("pmf.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createQueryResponse(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);

    Template temp = cfg.getTemplate("queryresponse.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createServiceImplSlim3(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("serviceimplslim3.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createServiceImplObjectify(Class<?> pojoType, String pack, OutputStream output)
      throws IOException, TemplateException {
    String pojoName = pojoType.getSimpleName();

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoType", pojoType.getCanonicalName());
    root.put("pojoName", pojoName);
    root.put("pojoNameLowerCase", pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1));
    root.put("keyType", Long.class.getSimpleName());
    root.put("keyPath", "getId()");

    Template temp = cfg.getTemplate("serviceimplobjectify.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }
}
