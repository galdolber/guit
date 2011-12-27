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
package com.guit.rebind.gin;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.rebind.adapter.GinModuleAdapter;
import com.google.inject.Module;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GinOracle {

  public static final String className = "GeneratedGinInjector";
  public static final String packageName = "com.guit.client";
  public static final String fullName = packageName + "." + className;

  private static HashMap<String, String> linkedBindings = new HashMap<String, String>();
  private static HashMap<String, String> inverseLinkedBindings = new HashMap<String, String>();
  private static List<Module> gmodules;

  private static List<Module> createModules(Set<Class<? extends GinModule>> gmodules) {
    List<Module> modules = new ArrayList<Module>();
    for (Class<? extends GinModule> moduleClass : gmodules) {
      Module module;
      module = instantiateGModuleClass(moduleClass);
      if (module != null) {
        modules.add(module);
      }
    }
    return modules;
  }

  /**
   * Finds the gin implementation.
   */
  public static String findImplementation(String classOrInterface) {
    try {
      return getImplementation(classOrInterface);
    } catch (Exception e) {
      return null;
    }
  }

  public static String getAsyncProvidedInstance(String classType) {
    return fullName + ".SINGLETON." + getAsyncProviderGetterMethodName(classType) + "()";
  }

  public static String getAsyncProviderGetterMethodName(String classType) {
    return getGetterMethodName(classType) + "_AsyncProvider";
  }

  public static String getClassOrLinkedInjectionKey(String classType) {
    if (inverseLinkedBindings.containsKey(classType)) {
      return inverseLinkedBindings.get(classType);
    }
    return classType;
  }

  public static List<Element> getElements() {
    return Elements.getElements(gmodules);
  }

  public static List<Element> getElementsForModules(Set<Class<? extends GinModule>> gmodules) {
    return Elements.getElements(createModules(gmodules));
  }

  public static String getGetterMethodName(String classType) {
    return getClassOrLinkedInjectionKey(classType).replaceAll("[.]", "_");
  }

  /**
   * Get the gin implementation.
   */
  public static String getImplementation(String classOrInterface) {
    if (linkedBindings.containsKey(classOrInterface)) {
      return linkedBindings.get(classOrInterface);
    } else {
      throw new IllegalStateException("The class " + classOrInterface
          + " is not binded in gin as a LinkedBinding");
    }
  }

  public static String getInjectedInstance(String classType) {
    return fullName + ".SINGLETON." + getGetterMethodName(classType) + "()";
  }

  private static void getLinkedBindings(Set<Class<? extends GinModule>> gmodules) {
    List<Element> elements;
    elements = getElementsForModules(gmodules);

    linkedBindings.clear();
    inverseLinkedBindings.clear();
    for (Element e : elements) {
      if (e instanceof LinkedBindingImpl<?>) {
        LinkedBindingImpl<?> b = (LinkedBindingImpl<?>) e;
        String key = b.getKey().getTypeLiteral().toString();
        String value = b.getLinkedKey().getTypeLiteral().toString();
        linkedBindings.put(key, value);
        inverseLinkedBindings.put(value, key);
      }
    }
  }

  public static String getProvidedInstance(String classType) {
    return fullName + ".SINGLETON." + getProviderGetterMethodName(classType) + "()";
  }

  public static String getProviderGetterMethodName(String classType) {
    return getGetterMethodName(classType) + "_Provider";
  }

  private static Module instantiateGModuleClass(Class<? extends GinModule> moduleClassName) {
    try {
      return new GinModuleAdapter(moduleClassName.newInstance());
    } catch (InstantiationException e) {
      return null;
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  public static void setModules(HashSet<Class<? extends GinModule>> gmodules) {
    GinOracle.gmodules = createModules(gmodules);
    getLinkedBindings(gmodules);
  }
}
