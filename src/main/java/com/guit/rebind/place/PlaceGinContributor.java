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

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.Element;

import com.guit.client.binder.contributor.RunAsync;
import com.guit.client.place.Place;
import com.guit.rebind.gin.GinContext;
import com.guit.rebind.gin.GinContributor;
import com.guit.rebind.gin.GinOracle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaceGinContributor implements GinContributor, PlaceProcessor {

  // Shared instance of places. For the generation
  protected static ArrayList<Class<?>> places = new ArrayList<Class<?>>();

  private static final Class<?> placeClassType = Place.class;
  private static final Class<Singleton> singletonClassType = Singleton.class;

  private GinContext ginContext;

  @Override
  public void collaborate(GinContext ginContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    this.ginContext = ginContext;

    places.clear();
    List<Element> elements = GinOracle.getElementsForModules(ginContext.getGModules());
    processPlaces(logger, elements, this);
  }

  @Override
  public void process(Class<?> placeType) {
    places.add(placeType);

    String canonicalName = placeType.getCanonicalName();
    if (placeType.isAnnotationPresent(RunAsync.class)) {
      ginContext.addAsyncProvidedType(canonicalName);
    } else {
      ginContext.addProvidedType(canonicalName);
    }
  }

  private void processPlaces(TreeLogger logger, List<Element> elements,
      PlaceProcessor placeProcessor) throws UnableToCompleteException {
    for (Element e : elements) {
      if (e instanceof BindingImpl<?>) {
        BindingImpl<?> binding = (BindingImpl<?>) e;
        Type type = ((TypeLiteral<?>) binding.getKey().getTypeLiteral()).getType();
        if (type instanceof Class<?> && placeClassType.isAssignableFrom((Class<?>) type)) {
          Class<?> placeType = (Class<?>) type;

          // TODO What about @Singleton annotation?
          Scoping scoping = binding.getScoping();
          if (scoping != null) {
            Class<? extends Annotation> scopeAnnotation = scoping.getScopeAnnotation();
            if (scopeAnnotation != null && scopeAnnotation.equals(singletonClassType)) {
              placeProcessor.process(placeType);
              continue;
            }
          }

          logger.log(com.google.gwt.core.ext.TreeLogger.Type.ERROR,
              "All places should have Singleton scope(declared in module, not as Annotation). Found: "
                  + placeType.getCanonicalName() + " Scope: " + binding.getScoping());
          throw new UnableToCompleteException();
        }
      }
    }
  }
}
