package com.guit.junit.dom;

import com.guit.client.dom.Optgroup;

public class OptgroupMock extends ElementMock implements Optgroup {

  public OptgroupMock() {
    super("optgroup");
  }

  @Override
  public String label() {
    return attr("label");
  }

  @Override
  public boolean disabled() {
    return attr("disabled").equals("disabled");
  }

  @Override
  public void disabled(boolean disabled) {
    if (disabled) {
      setDisabled("disabled");
    } else {
      setDisabled("");
    }
  }

  @Override
  public void setDisabled(String disabled) {
    attr("disabled", "disabled");
  }

  @Override
  public void label(String label) {
    attr("label", label);
  }
}
