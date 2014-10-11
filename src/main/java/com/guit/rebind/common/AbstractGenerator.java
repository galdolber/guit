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
package com.guit.rebind.common;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.CachedPropertyInformation;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.IncrementalGenerator;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.RebindMode;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.ResourceOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.rpc.CachedRpcTypeInformation;

import java.io.PrintWriter;

public abstract class AbstractGenerator extends IncrementalGenerator {

  protected TreeLogger logger;
  protected GeneratorContext context;
  protected JClassType baseClass;
  protected TypeOracle typeOracle;
  protected ResourceOracle resourceOracle;
  protected PropertyOracle propertiesOracle;
  protected String postfix = "Impl";
  protected String generatedPackage;
  protected String implName;
  protected String typeName;
  protected String createdClassName;
  protected String implementationPostfix = "Impl";

  protected ClassSourceFileComposerFactory createComposer()
      throws UnableToCompleteException {
    return new ClassSourceFileComposerFactory(generatedPackage, implName);
  }

  protected PrintWriter createPrintWriter() throws UnableToCompleteException {
    return context.tryCreate(logger, generatedPackage, implName);
  }

  protected void error(String message, Object... params)
      throws UnableToCompleteException {
    logger.log(TreeLogger.ERROR, String.format(message, params));
    throw new UnableToCompleteException();
  }

  protected Class<?> findClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public ConfigurationProperty findConfigurationProperty(String propertyName) {
    try {
      return propertiesOracle.getConfigurationProperty(propertyName);
    } catch (BadPropertyValueException e) {
      return null;
    }
  }

  public SelectionProperty findSelectionProperty(String propertyName) {
    try {
      return propertiesOracle.getSelectionProperty(logger, propertyName);
    } catch (BadPropertyValueException e) {
      return null;
    }
  }

  protected abstract void generate(SourceWriter writer)
      throws UnableToCompleteException;

  @Override
  public RebindResult generateIncrementally(TreeLogger logger,
      GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    saveVariables(logger, context, typeName);

    baseClass = getType(typeName);
    generatedPackage = baseClass.getPackage().getName();
    implName = baseClass.getSimpleSourceName() + implementationPostfix;
    implName = processImplName(implName);
    JClassType enclosingType = baseClass.getEnclosingType();
    if (enclosingType != null) {
      implName = enclosingType.getSimpleSourceName() + implName;
    }

    RebindMode rebindMode = context.isGeneratorResultCachingEnabled() ? rebindMode() : RebindMode.USE_ALL_NEW_WITH_NO_CACHING;
    if (rebindMode.equals(RebindMode.USE_ALL_CACHED) || rebindMode.equals(RebindMode.USE_EXISTING)) {
      return new RebindResult(rebindMode, generatedPackage + "." + implName);
    }
    
    ClassSourceFileComposerFactory composer = createComposer();
    processComposer(composer);
    createdClassName = composer.getCreatedClassName();
    PrintWriter printWriter = createPrintWriter();
    if (printWriter != null) {
      SourceWriter writer = composer.createSourceWriter(context, printWriter);
      generate(writer);

      writer.commit(logger);
    }
    
    RebindResult result = new RebindResult(rebindMode, composer.getCreatedClassName());
    saveClientData(result);
    return result;
  }

  protected RebindMode rebindMode() {
    return RebindMode.USE_ALL_NEW_WITH_NO_CACHING;
  }

  protected void saveClientData(RebindResult result) {
  }

  protected String processImplName(String implName) {
    SelectionProperty s = findSelectionProperty("ui.xml.prefix");
    String prefix = s.getCurrentValue();
    return prefix + implName;
  }

  public ConfigurationProperty getConfigurationProperty(String propertyName) {
    try {
      return propertiesOracle.getConfigurationProperty(propertyName);
    } catch (BadPropertyValueException e) {
      throw new IllegalStateException(e);
    }
  }

  protected JPackage getPackage(String pkgName) {
    return typeOracle.findPackage(pkgName);
  }

  protected JClassType getType(String typeName) {
    return typeOracle.findType(typeName);
  }

  protected <T> T newInstance(Class<? extends T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      return null;
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  protected abstract void processComposer(
      ClassSourceFileComposerFactory composer);

  protected void saveVariables(TreeLogger logger, GeneratorContext context,
      String typeName) {
    this.logger = logger;
    this.context = context;
    this.typeName = typeName;

    typeOracle = context.getTypeOracle();
    resourceOracle = context.getResourcesOracle();
    propertiesOracle = context.getPropertyOracle();
  }

  protected void warn(String message, Object... params) {
    logger.log(TreeLogger.WARN, String.format(message, params));
  }

  public TreeLogger getLogger() {
    return logger;
  }

  @Override
  public long getVersionId() {
    return 1L;
  }
}
