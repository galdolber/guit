/*
 * Copyright 2010 Gal Dolber.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.client.binder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import com.guit.client.GuitEntryPoint;
import com.guit.client.async.event.AsyncExceptionEvent;
import com.guit.client.binder.useraction.event.UserActionEvent;
import com.guit.client.binder.useraction.event.UserActionHandler;
import com.guit.client.common.HasState;

import java.util.Date;

public class GuitBinderGwtTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.guit.client.binder.TestBinderModule";
  }

  private TestPresenter presenter;

  private EditorPresenter editorPresenter;

  private WidgetPresenter widgetPresenter;

  private EventBus eventBus;

  @Override
  protected void gwtSetUp() throws Exception {
    GuitEntryPoint entryPoint = new GuitEntryPoint();
    entryPoint.onModuleLoad();
    presenter = TestPresenter.instance;
    editorPresenter = EditorPresenter.instance;
    widgetPresenter = WidgetPresenter.instance;
    eventBus = GuitEntryPoint.getEventBus();
  }

  public void testElementsClick() {
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);

    widgetPresenter.divclicked = false;
    widgetPresenter.div.getElement().dispatchEvent(event);
    assertTrue(widgetPresenter.divclicked);

    widgetPresenter.div1clicked = false;
    widgetPresenter.div1.getElement().dispatchEvent(event);
    assertTrue(widgetPresenter.div1clicked);

    widgetPresenter.spanclicked = false;
    widgetPresenter.span.getElement().dispatchEvent(event);
    assertTrue(widgetPresenter.spanclicked);
  }

  public void testViewFieldsBindings() {
    assertNotNull(presenter.button);
    assertTrue(presenter.button instanceof Button);

    assertNotNull(presenter.button2);
    assertTrue(presenter.button2 instanceof Button);

    assertNotNull(presenter.anotherButton2Bind);
    assertTrue(presenter.anotherButton2Bind instanceof Button);
    assertEquals(presenter.button2, presenter.anotherButton2Bind);

    assertNotNull(presenter.runasync);
    assertTrue(presenter.runasync instanceof Button);

    assertNotNull(presenter.div);
    assertFalse(presenter.div instanceof Element); // Is emulated
  }

  public void testConventionViewHandler() {
    presenter.convention$singlefield$click$triggered = false;
    ((Button) presenter.button).click();
    assertTrue(presenter.convention$singlefield$click$triggered);
  }

  public void testAnnotationViewHandler() {
    presenter.annotation$singlefield$click$triggered = false;
    ((Button) presenter.button).click();
    assertTrue(presenter.annotation$singlefield$click$triggered);
  }

  public void testConventionMultiFieldsHandler() {
    presenter.convention$manyfields$click$triggered = false;
    ((Button) presenter.button).click();
    assertTrue(presenter.convention$manyfields$click$triggered);

    presenter.convention$manyfields$click$triggered = false;
    ((Button) presenter.button2).click();
    assertTrue(presenter.convention$manyfields$click$triggered);
  }

  public void testAnnotationMultiFieldsHandler() {
    presenter.annotation$manyfields$click$triggered = false;
    ((Button) presenter.button).click();
    assertTrue(presenter.annotation$manyfields$click$triggered);

    presenter.annotation$manyfields$click$triggered = false;
    ((Button) presenter.button2).click();
    assertTrue(presenter.annotation$manyfields$click$triggered);
  }

  public void testRunAsyncViewHandler() {
    presenter.runasync$triggered = false;
    ((Button) presenter.runasync).click();
    assertTrue(presenter.runasync$triggered);
  }

  public void testUserActionViewHandler() {
    final HasState<Boolean> state = new HasState<Boolean>();
    state.value = false;
    eventBus.addHandler(UserActionEvent.getType(), new UserActionHandler() {
      @Override
      public void onUserAction(UserActionEvent event) {
        assertEquals("Test", event.getUserAction());
        assertEquals("TestCategory", event.getCategory());
        assertEquals("3", event.getText());
        assertEquals(3, (int) event.getNumber());
        state.value = true;
      }
    });
    presenter.userAction$triggered = false;
    ((Button) presenter.userAction).click();
    assertTrue(presenter.userAction$triggered);
    assertTrue(state.value);
  }

  public void testHasAttributeAndAttibuteAnnotationsHandler() {
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);

    presenter.hasAttribute$triggered = null;
    ((Element) presenter.trueElement.getTarget()).dispatchEvent(event);
    assertTrue(presenter.hasAttribute$triggered);

    presenter.hasAttribute$triggered = null;
    ((Element) presenter.falseElement.getTarget()).dispatchEvent(event);
    assertFalse(presenter.hasAttribute$triggered);
  }

  public void testKeyCodeViewHandler() {
    presenter.keyCode$triggered = false;
    NativeEvent event =
        Document.get().createKeyDownEvent(false, false, false, false, KeyCodes.KEY_ENTER);
    KeyPressEvent.fireNativeEvent(event, ((TextBox) presenter.textbox));
    assertTrue(presenter.keyCode$triggered);

    presenter.keyCode$triggered = false;
    event = Document.get().createKeyDownEvent(false, false, false, false, KeyCodes.KEY_ALT);
    KeyPressEvent.fireNativeEvent(event, ((TextBox) presenter.textbox));
    assertFalse(presenter.keyCode$triggered);
  }

  public void testPreventDefaultViewHandler() {
    presenter.preventDefault$triggered = false;
    ((Button) presenter.preventDefault).click();
    assertTrue(presenter.preventDefault$triggered);
  }

  public void testStopPropagationViewHandler() {
    presenter.stopPropagation$triggered = false;
    presenter.stopPropagationPanel$triggered = false;
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    ((Label) presenter.stopPropagation).getElement().dispatchEvent(event);
    assertTrue(presenter.stopPropagation$triggered);
    assertFalse(presenter.stopPropagationPanel$triggered);
  }

  public void testDontStopPropagationViewHandler() {
    presenter.dontStopPropagation$triggered = false;
    presenter.dontStopPropagationPanel$triggered = false;
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    ((Label) presenter.dontStopPropagation).getElement().dispatchEvent(event);
    assertTrue(presenter.dontStopPropagation$triggered);
    assertTrue(presenter.dontStopPropagationPanel$triggered);
  }

  public void testHtmlElements() {
    String src = "http://imagesrc";
    presenter.image.setSrc(src);
    assertEquals(src, presenter.image.getSrc());
  }

  public void testAutofocus() {
    assertTrue(presenter.focus);
  }

  public void testEditor() {
    editorPresenter.getView();
    editorPresenter.driver.edit(new Pojo());

    long time = 1296893958140L;
    Date birthday = new Date(time);
    editorPresenter.birthday.setValue(birthday);
    Boolean enabled = false;
    editorPresenter.enabled.setValue(enabled);
    String name = "Einstein";
    editorPresenter.name.setValue(name);
    Integer votes = 33;
    editorPresenter.votes.setValue(votes);
    Double age = 33.33;
    editorPresenter.age.setValue(age);

    Pojo pojo = editorPresenter.driver.flush();
    assertEquals(enabled, pojo.getEnabled());
    assertEquals(name, pojo.getName());
    assertEquals(votes, pojo.getVotes());
    assertEquals(age, pojo.getAge());

    // assertEquals(time, pojo.getBirthday().getTime());
  }

  public void testEventBus() {
    presenter.asyncActivity = false;
    eventBus.fireEvent(new AsyncExceptionEvent(null));
    assertTrue(presenter.asyncActivity);
  }
}
