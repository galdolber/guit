package com.guit.client.binder;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Singleton;

import com.guit.client.GuitPresenter;
import com.guit.client.binder.EditorPresenter.EditorPresenterBinder;

import java.util.Date;

@GwtEditor(pojo = Pojo.class)
@Singleton
public class EditorPresenter extends GuitPresenter<EditorPresenterBinder> {
  public interface EditorPresenterBinder extends GuitBinder<EditorPresenter> {
  }

  public static EditorPresenter instance;

  @Override
  protected void initialize() {
    instance = this;
  }

  SimpleBeanEditorDriver<Pojo, ?> driver;

  // The following @ViewField's are not necessary for the driver
  @ViewField
  HasValue<String> name;
  @ViewField
  HasValue<Double> age;
  @ViewField
  HasValue<Date> birthday;
  @ViewField
  HasValue<Boolean> enabled;
  @ViewField
  HasValue<Integer> votes;
}
