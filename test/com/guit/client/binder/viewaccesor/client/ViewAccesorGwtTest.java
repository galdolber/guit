package com.guit.client.binder.viewaccesor.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;

import com.guit.client.GuitEntryPoint;
import com.guit.client.binder.viewaccesor.client.ViewAccesorPresenter.AccesorWithImplementationImpl;
import com.guit.client.binder.viewaccesor.client.ViewAccesorPresenter.AccesorWithoutImplementationImpl;
import com.guit.client.dom.Element;
import com.guit.client.dom.impl.ElementImpl;

import org.junit.Test;

public class ViewAccesorGwtTest extends GWTTestCase {

  ViewAccesorPresenter presenter;

  @Override
  public String getModuleName() {
    return "com.guit.client.binder.viewaccesor.TestViewAccesorModule";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    GuitEntryPoint entryPoint = new GuitEntryPoint();
    entryPoint.onModuleLoad();
    presenter = ViewAccesorPresenter.instance;
  }

  @Test
  public void test() {
    Element e = new ElementImpl(DOM.createDiv());
    e.html("<input name='name' />");
    assertEquals(1, e.query("input[name='name']").size());
  }

  @Test
  public void testAccesorInstances() {
    assertNotNull(presenter.with);
    assertNotNull(presenter.without);

    assertTrue(presenter.with instanceof AccesorWithImplementationImpl);
    assertTrue(presenter.without instanceof AccesorWithoutImplementationImpl);
  }

  @Test
  public void testDiv() {
    assertEquals("Hello world", presenter.div.text());
  }
}
