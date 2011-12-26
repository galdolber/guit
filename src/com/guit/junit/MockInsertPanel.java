package com.guit.junit;

import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class MockInsertPanel implements InsertPanel.ForIsWidget {

  private final ArrayList<Widget> widgets = new ArrayList<Widget>();

  @Override
  public void add(Widget w) {
    widgets.add(w);
  }

  @Override
  public void insert(Widget w, int beforeIndex) {
    widgets.add(beforeIndex, w);
  }

  @Override
  public Widget getWidget(int index) {
    return widgets.get(index);
  }

  @Override
  public int getWidgetCount() {
    return widgets.size();
  }

  @Override
  public int getWidgetIndex(Widget child) {
    return widgets.indexOf(child);
  }

  @Override
  public boolean remove(int index) {
    return widgets.remove(widgets.get(index));
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return widgets.indexOf(child);
  }

  @Override
  public void add(IsWidget w) {
    widgets.add(w.asWidget());
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    widgets.add(beforeIndex, w.asWidget());
  }
}
