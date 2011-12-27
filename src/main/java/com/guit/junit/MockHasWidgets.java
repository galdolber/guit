package com.guit.junit;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Iterator;

public class MockHasWidgets implements HasWidgets.ForIsWidget {

  private final ArrayList<Widget> widgets = new ArrayList<Widget>();

  @Override
  public void add(Widget w) {
    widgets.add(w);
  }

  @Override
  public void clear() {
    widgets.clear();
  }

  @Override
  public Iterator<Widget> iterator() {
    return widgets.iterator();
  }

  @Override
  public boolean remove(Widget w) {
    return widgets.remove(w);
  }

  @Override
  public void add(IsWidget w) {
    widgets.add(w.asWidget());
  }

  @Override
  public boolean remove(IsWidget w) {
    return widgets.remove(w.asWidget());
  }
}
