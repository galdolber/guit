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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import com.guit.client.GuitPresenter;
import com.guit.client.Implementation;
import com.guit.client.ViewAccesor;
import com.guit.client.apt.GwtPresenter;
import com.guit.client.async.event.AsyncExceptionEvent;
import com.guit.client.binder.SomePresenter.TestBinder;
import com.guit.client.binder.contributor.HasAttribute;
import com.guit.client.binder.contributor.KeyCode;
import com.guit.client.binder.contributor.PreventDefault;
import com.guit.client.binder.contributor.RunAsync;
import com.guit.client.binder.contributor.StopPropagation;
import com.guit.client.binder.contributor.UserAction;
import com.guit.client.binder.useraction.Category;
import com.guit.client.binder.useraction.HasUserActionValue;

@Category("TestCategory")
@GwtPresenter(autofocus = "textbox", title = "Test")
@Singleton
public class SomePresenter extends GuitPresenter<TestBinder> implements HasUserActionValue {

  public interface HasInnerHTML {

    String getInnerHTML();

    void setInnerHTML(String html);
  }

  @Implementation(SomeAccesorImpl.class)
  public static interface SomeAccesor extends ViewAccesor {
    Object getTarget();
  }

  public static class SomeAccesorImpl implements SomeAccesor {

    private Object target;

    @Override
    public void setTarget(Object target) {
      this.target = target;
    }

    @Override
    public Object getTarget() {
      return target;
    }
  }

  public static interface TestBinder extends GuitBinder<SomePresenter> {
  }

  public static SomePresenter instance;

  @Override
  protected void initialize() {
    // Should not do this in real code
    RootPanel.get().add((Widget) getView());
    instance = this;
  }

  @ViewField
  HasText button;

  @ViewField
  HasText button2;

  @ViewField(name = "button2")
  HasText anotherButton2Bind;

  @ViewField
  HasText runasync;

  @ViewField(provided = true)
  HasText provided = new Button("Dont do this!");

  @ViewField
  HasText preventDefault;

  @ViewField(provided = true)
  TextBox providedTextbox = new TextBox();

  @ViewField
  HasText userAction;

  @ViewField
  HasInnerHTML div;

  @ViewField
  HasHTML html;

  @ViewField
  HasText stopPropagation;

  @ViewField
  HasText dontStopPropagation;

  @ViewField
  SomeAccesor trueElement;

  @ViewField
  SomeAccesor falseElement;

  @ViewField
  HasText textbox;

  @ViewField
  HasSrc image;

  boolean focus = false;

  boolean convention$singlefield$click$triggered;
  boolean annotation$singlefield$click$triggered;
  boolean convention$manyfields$click$triggered;
  boolean annotation$manyfields$click$triggered;

  boolean runasync$triggered;
  Boolean hasAttribute$triggered;
  boolean userAction$triggered;
  boolean keyCode$triggered;
  boolean preventDefault$triggered;
  boolean stopPropagation$triggered;
  boolean stopPropagationPanel$triggered;
  boolean dontStopPropagation$triggered;
  boolean dontStopPropagationPanel$triggered;

  boolean asyncActivity;

  @ViewHandler
  public void button$click() {
    convention$singlefield$click$triggered = true;
  }

  @ViewHandler(event = ClickEvent.class, fields = "button")
  public void buttonClicked() {
    annotation$singlefield$click$triggered = true;
  }

  @ViewHandler
  public void button$button2$click() {
    convention$manyfields$click$triggered = true;
  }

  @ViewHandler(event = ClickEvent.class, fields = {"button", "button2"})
  public void buttonsClicked() {
    annotation$manyfields$click$triggered = true;
  }

  @ViewHandler
  @RunAsync
  public void runasync$click() {
    runasync$triggered = true;
  }

  @ViewHandler
  @UserAction("Test")
  public void userAction$click() {
    userAction$triggered = true;
  }

  @ViewHandler
  @HasAttribute("value")
  public void html$click(@Attribute Boolean value) {
    hasAttribute$triggered = value;
  }

  @Override
  public int getUserActionNumber() {
    return 3;
  }

  @Override
  public String getUserActionText() {
    return "3";
  }

  @ViewHandler
  @KeyCode(KeyCodes.KEY_ENTER)
  public void textbox$keyDown() {
    keyCode$triggered = true;
  }

  @ViewHandler
  @PreventDefault
  public void preventDefault$click() {
    preventDefault$triggered = true;
  }

  @ViewHandler
  @StopPropagation
  public void stopPropagation$click() {
    stopPropagation$triggered = true;
  }

  @ViewHandler
  public void stopPropagationPanel$click() {
    stopPropagationPanel$triggered = true;
  }

  @ViewHandler
  public void dontStopPropagation$click() {
    dontStopPropagation$triggered = true;
  }

  @ViewHandler
  public void dontStopPropagationPanel$click() {
    dontStopPropagationPanel$triggered = true;
  }

  @EventBusHandler(AsyncExceptionEvent.class)
  public void $asyncException() {
    asyncActivity = true;
  }

  @ViewHandler
  void textbox$focus() {
    focus = true;
  }
}
