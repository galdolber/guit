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

import com.google.gwt.editor.client.SimpleBeanEditorDriver;

/**
 * The generated view is an editor.
 */
public @interface GwtEditor {

  /**
   * @return The base type used for the driver.
   */
  Class<?> base() default SimpleBeanEditorDriver.class;

  /**
   * @return Editor driver pojo
   */
  Class<?> pojo();

  /**
   * Each entry represents the fields name, equal("="), the path. i.e:
   * @GwtEditor(pojo=Person.class, paths={"address=data.address"})
   * 
   * @return Editor paths
   */
  String[] paths() default {};
}
