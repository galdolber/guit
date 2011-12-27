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
package com.guit.client;

/**
 * View types. Affects how uibinder is declared.
 */
public enum ViewType {
  /**
   * The view is created as a composite and we bind the uibinder as a widget and
   * call initWidget with it.
   */
  COMPOSITE,

  /**
   * The view is created as a widget and we bind the uibinder as an element and
   * call setElement with it.
   */
  WIDGET,

  /**
   * We just bind the uibinder and don't wrap it with anything. This is useful
   * to create a PopupPanel.
   */
  ROOTLESS
}