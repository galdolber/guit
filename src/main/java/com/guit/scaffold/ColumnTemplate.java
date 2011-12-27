package com.guit.scaffold;

/**
 * Representation of a column template.
 */
public class ColumnTemplate {

  private String fieldName;
  private String columnType;
  private String getter;
  private String cellType;
  private Boolean toString;

  public ColumnTemplate(String fieldName, String getter, String columnType, String cellType,
      Boolean toString) {
    this.setFieldName(fieldName);
    this.setGetter(getter);
    this.setColumnType(columnType);
    this.setCellType(cellType);
    this.setToString(toString);
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }

  public String getColumnType() {
    return columnType;
  }

  public void setGetter(String getter) {
    this.getter = getter;
  }

  public String getGetter() {
    return getter;
  }

  public void setCellType(String cellType) {
    this.cellType = cellType;
  }

  public String getCellType() {
    return cellType;
  }

  public void setToString(Boolean toString) {
    this.toString = toString;
  }

  public Boolean getToString() {
    return toString;
  }
}
