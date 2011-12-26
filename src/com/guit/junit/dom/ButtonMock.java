package com.guit.junit.dom;

import com.guit.client.dom.Button;

public class ButtonMock extends BaseMock implements Button {

  public ButtonMock() {
    super("button");
  }

  @Override
  public void click() {
  }
}
