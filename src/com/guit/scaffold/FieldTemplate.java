package com.guit.scaffold;

public class FieldTemplate {

  private String name;
  private String tag;
  private String label;

  public FieldTemplate(String name, String tag, String label) {
    this.setName(name);
    this.setTag(tag);
    this.setLabel(label);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
