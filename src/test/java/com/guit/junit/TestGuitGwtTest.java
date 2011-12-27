package com.guit.junit;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import org.junit.Test;

public class TestGuitGwtTest extends GuitTest {

  @Inject
  IsWidget isWidget;

  @Override
  protected void configure() {
    mock(IsWidget.class);
  }

  @Test
  public void test() {
    isWidget.hashCode();
  }
}
