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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.inject.Provider;

import com.guit.rebind.common.AbstractGenerator;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

/**
 * This one is static to be accesible from any Generator, event outsiders.
 */
public class GinInjectorGenerator extends AbstractGenerator implements GinContext {

  private static final String SINGLETON_DECLARATION = "public static " + GinOracle.fullName
      + " SINGLETON = " + GWT.class.getCanonicalName() + ".create(" + GinOracle.fullName
      + ".class);";

  private HashSet<String> injectedClasses = new HashSet<String>();

  private HashSet<String> providedClasses = new HashSet<String>();

  private HashSet<String> asyncProvidedClasses = new HashSet<String>();

  private final HashSet<Class<? extends GinModule>> gmodules =
      new HashSet<Class<? extends GinModule>>();

  @Override
  public void addAsyncProvidedType(String classType) {
    asyncProvidedClasses.add(classType);
  }

  @Override
  public void addInjectedType(String classType) {
    injectedClasses.add(classType);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addModule(String module) {
    try {
      gmodules.add((Class<? extends GinModule>) Class.forName(module));
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void addProvidedType(String classType) {
    providedClasses.add(classType);
  }

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
  }

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    saveVariables(logger, context, typeName);

    // Clear
    injectedClasses.clear();
    providedClasses.clear();
    asyncProvidedClasses.clear();
    gmodules.clear();

    // Call gin contributors
    List<String> contributors = getConfigurationProperty("app.gin.contributor").getValues();
    for (String c : contributors) {
      GinContributor contributor = instantiateContributor(c);
      contributor.collaborate(this, logger, context);
    }

    // Generate the modules string
    StringBuilder sb = new StringBuilder();
    sb.append("({");
    for (Class<?> m : gmodules) {
      if (sb.length() > 2) {
        sb.append(", ");
      }
      sb.append(m.getCanonicalName() + ".class");
    }
    sb.append("})");

    GinOracle.setModules(gmodules);

    ClassSourceFileComposerFactory composer =
        new ClassSourceFileComposerFactory(GinOracle.packageName, GinOracle.className);
    composer.makeInterface();
    composer.addImplementedInterface(Ginjector.class.getCanonicalName());
    composer.addAnnotationDeclaration("@" + GinModules.class.getCanonicalName() + sb.toString());
    PrintWriter printWriter = context.tryCreate(logger, GinOracle.packageName, GinOracle.className);

    // Convert to linked to remove possible duplicated entries
    injectedClasses = findClassOrLinkedInjectionKey(injectedClasses);
    providedClasses = findClassOrLinkedInjectionKey(providedClasses);
    asyncProvidedClasses = findClassOrLinkedInjectionKey(asyncProvidedClasses);

    if (printWriter != null) {
      SourceWriter writer = composer.createSourceWriter(context, printWriter);

      writer.println(SINGLETON_DECLARATION);

      for (String classType : injectedClasses) {
        writer.println(classType + " " + GinOracle.getGetterMethodName(classType) + "();");
      }

      for (String classType : providedClasses) {
        writer.println(Provider.class.getCanonicalName() + "<" + classType + "> "
            + GinOracle.getProviderGetterMethodName(classType) + "();");
      }

      for (String classType : asyncProvidedClasses) {
        writer.println(AsyncProvider.class.getCanonicalName() + "<" + classType + "> "
            + GinOracle.getAsyncProviderGetterMethodName(classType) + "();");
      }

      writer.commit(logger);
    }

    return null;
  }

  private HashSet<String> findClassOrLinkedInjectionKey(HashSet<String> original) {
    HashSet<String> set = new HashSet<String>();
    for (String c : original) {
      set.add(GinOracle.getClassOrLinkedInjectionKey(c));
    }
    return set;
  }

  @Override
  public HashSet<String> getAsyncProvidedClasses() {
    return asyncProvidedClasses;
  }

  @Override
  public HashSet<Class<? extends GinModule>> getGModules() {
    return gmodules;
  }

  @Override
  public HashSet<String> getInjectedClasses() {
    return injectedClasses;
  }

  @Override
  public HashSet<String> getProvidedClasses() {
    return providedClasses;
  }

  private GinContributor instantiateContributor(String c) {
    try {
      Class<?> clazz = Class.forName(c);
      return (GinContributor) clazz.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImplementedInterface(typeName);
  }
}
