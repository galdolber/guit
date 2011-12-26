package com.guit.rebind.guitview;

public class GuitViewField {

  private final String name;
  private final String type;
  private final String declaration;

  public GuitViewField(String name, String type, String declaration) {
    this.name = name;
    this.type = type;
    this.declaration = declaration;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getDeclaration() {
    return declaration;
  }
}
