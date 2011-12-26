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
package com.guit.rebind.place;

import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JGenericType;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.contributor.RunAsync;
import com.guit.client.place.Place;
import com.guit.client.place.PlaceDataEncrypted;
import com.guit.client.place.PlaceManagerImpl;
import com.guit.rebind.common.AbstractGenerator;
import com.guit.rebind.gin.GinOracle;
import com.guit.rebind.jsorm.JsonSerializerUtil;

import java.util.List;

public class PlaceManagerInitializerGenerator extends AbstractGenerator {

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    writer.println("public void initialize(" + PlaceManagerImpl.class.getCanonicalName()
        + " manager) {");
    writer.indent();

    boolean encryptAll =
        !findConfigurationProperty("app.encrypt.place").getValues().get(0).equals("false");

    for (Class<?> p : PlaceGinContributor.places) {
      String canonicalName = p.getCanonicalName();
      String injectedPlace =
          p.isAnnotationPresent(RunAsync.class) ? GinOracle.getAsyncProvidedInstance(canonicalName)
              : GinOracle.getProvidedInstance(p.getCanonicalName());

      String encryptPlace =
          (encryptAll || p.isAnnotationPresent(PlaceDataEncrypted.class)) ? "true" : "false";

      writer.println("manager.addPlace(" + canonicalName + ".class, \""
          + getPlaceName(p.getCanonicalName()) + "\", \"" + getPlaceTitle(p) + "\", "
          + injectedPlace + ", " + generatePlaceData(logger, context, canonicalName)
          + ".getSingleton(), " + encryptPlace + ");");
    }

    ConfigurationProperty configurationProperty = findConfigurationProperty("app.default.place");
    if (configurationProperty != null) {
      List<String> defaultPlaces = configurationProperty.getValues();
      if (defaultPlaces.size() == 1) {
        String defaultPlace = defaultPlaces.get(0);
        if (defaultPlace != null) {
          JClassType type = getType(defaultPlace);
          if (type == null) {
            error(
                "The default place type does not exists, check your gwt.xml module for typos in 'app.default.place'. Found: %s",
                defaultPlace);
          }

          writer.println("manager.setDefaultPlace(\"" + getPlaceName(type.getQualifiedSourceName())
              + "\");");
        }
      } else {
        error("You can only have one default place");
      }
    }

    writer.println(History.class.getCanonicalName() + ".addValueChangeHandler(manager);");
    writer.println(Window.class.getCanonicalName() + ".addWindowClosingHandler(manager);");

    writer.outdent();
    writer.println("}");
  }

  private String getPlaceTitle(Class<?> p) throws UnableToCompleteException {
    String canonicalName = p.getCanonicalName();
    JClassType type = getType(GinOracle.getClassOrLinkedInjectionKey(canonicalName));
    GwtPresenter gwtPresenterAnnotation = getGwtAnnotation(type);
    if (gwtPresenterAnnotation != null) {
      return gwtPresenterAnnotation.title();
    } else {
      return "";
    }
  }

  private GwtPresenter getGwtAnnotation(JClassType type) {
    if (type.isAnnotationPresent(GwtPresenter.class)) {
      return type.getAnnotation(GwtPresenter.class);
    } else {
      if (type.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
        return null;
      }
      return getGwtAnnotation(type.getSuperclass());
    }
  }

  public String generatePlaceData(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    JGenericType placeType =
        context.getTypeOracle().findType(Place.class.getCanonicalName()).isGenericType();
    JClassType parameterType =
        context.getTypeOracle().findType(typeName).asParameterizationOf(placeType).getTypeArgs()[0];

    // We cannot serialize java.lang.Object
    if (parameterType.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
      error("You cannot serialize Object... we either. Found: %s", placeType
          .getQualifiedSourceName());
    }

    return JsonSerializerUtil.generate(logger, context, parameterType);
  }

  private String getPlaceName(String typeName) {
    JClassType type = getType(GinOracle.getClassOrLinkedInjectionKey(typeName));
    GwtPresenter gwtPresenterAnnotation = getGwtAnnotation(type);
    if (gwtPresenterAnnotation != null) {
      String placeName = gwtPresenterAnnotation.placeName();
      if (!placeName.isEmpty()) {
        return placeName;
      }
    }
    return toWebFriedly(type.getSimpleSourceName());
  }

  private String toWebFriedly(String simpleSourceName) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < simpleSourceName.length(); i++) {
      char charAt = simpleSourceName.charAt(i);
      if (Character.isLowerCase(charAt)) {
        sb.append(charAt);
      } else {
        if (sb.length() > 0) {
          sb.append("_");
        }
        sb.append(Character.toLowerCase(charAt));
      }
    }
    return sb.toString();
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImplementedInterface(typeName);
  }
}
