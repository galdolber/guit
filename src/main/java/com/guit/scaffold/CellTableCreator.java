package com.guit.scaffold;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.SingleSelectionModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates the base a celltable for a pojo.
 */
public class CellTableCreator extends Creator {

  public static void main(String[] args) throws IOException, TemplateException {
    new CellTableCreator().create(Person.class, Person.class.getPackage().getName(), System.out);
  }

  public void create(Class<?> pojoType, String pack, OutputStream output) throws IOException,
      TemplateException {

    ArrayList<ColumnTemplate> columns = new ArrayList<ColumnTemplate>();

    // Print columns
    String pojoName = pojoType.getSimpleName();
    HashSet<String> imports = new HashSet<String>();
    imports.add(CellTable.class.getCanonicalName());
    imports.add(Column.class.getCanonicalName());
    imports.add(pojoType.getCanonicalName());

    Method[] methods = pojoType.getDeclaredMethods();
    for (Method m : methods) {
      String getter = m.getName();
      if (!getter.startsWith("get")) {
        continue;
      }

      String fieldName = getter.substring(3);
      fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

      Class<?> cellType = null;
      String columnType = null;
      boolean toString = false;

      Class<?> fieldType = m.getReturnType();
      if (fieldType.equals(String.class)) {
        cellType = TextCell.class;
        columnType = "String";
      } else if (fieldType.equals(Integer.class) || fieldType.equals(Double.class)
          || fieldType.equals(Float.class) || fieldType.equals(Long.class)
          || fieldType.equals(int.class) || fieldType.equals(double.class)
          || fieldType.equals(float.class) || fieldType.equals(long.class)) {
        cellType = NumberCell.class;
        columnType = "Number";
      } else if (fieldType.equals(Date.class)) {
        imports.add(Date.class.getCanonicalName());
        cellType = DateCell.class;
        columnType = "Date";
      } else if (fieldType.isEnum()) {
        cellType = TextCell.class;
        columnType = "String";
        toString = true;
      } else {
        System.out.println("CellTableCreator: the type " + fieldType.getCanonicalName()
            + " is not supported");
      }

      if (cellType != null) {
        imports.add(cellType.getCanonicalName());
        columns.add(new ColumnTemplate(fieldName, getter, columnType, cellType.getSimpleName(),
            toString));
      }
    }

    imports.add(SingleSelectionModel.class.getCanonicalName());

    HashMap<String, Object> root = new HashMap<String, Object>();
    root.put("package", pack);
    root.put("pojoName", pojoName);
    root.put("imports", imports);
    root.put("columns", columns);

    Template temp = cfg.getTemplate("celltable.java.ftl");
    Writer out = new OutputStreamWriter(output);
    temp.process(root, out);
    out.close();
  }
}
