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
package com.guit.client.display;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Root layout display provider.
 */
@Singleton
public class RootLayoutPanelDisplayProvider implements Provider<AcceptsOneWidget>, AcceptsOneWidget {

  @Override
  public AcceptsOneWidget get() {
    return this;
  }

  @Override
  public void setWidget(IsWidget w) {
    RootLayoutPanel root = RootLayoutPanel.get();
    root.clear();
    root.add(w);
  }
}
