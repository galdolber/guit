package com.guit.scaffold;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class EnumEditorCreator extends Creator {

  public void createEnumEditor(Class<?> enumType, String pack, OutputStream output)
      throws IOException, TemplateException {
    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("enumType", enumType.getCanonicalName());
    root.put("enumName", enumType.getSimpleName());

    Template temp = cfg.getTemplate("enumeditor.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }

  public void createAbstractEnumEditor(String pack, OutputStream output) throws IOException,
      TemplateException {
    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);

    Template temp = cfg.getTemplate("abstractenumeditor.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }
}
