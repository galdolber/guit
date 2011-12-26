package com.guit.server.requestfactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class GuitServiceLayerDecorator extends ServiceLayerDecorator {

  /**
   * JSR 303 validator used to validate requested entities.
   * */
  private final Validator validator;
  private final Injector injector;

  /**
   * JSR 303 validator used to validate requested entities. / private final
   * Validator validator; private final Injector injector;
   * 
   * /** Creates new InjectableServiceLayer.
   */
  @Inject
  public GuitServiceLayerDecorator(Validator validator, Injector injector) {
    this.validator = validator;
    this.injector = injector;
  }

  /**
   * Uses Guice to create the instance of the target locator, so the locator
   * implementation could be injected.
   */
  @Override
  public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  /**
   * Invokes JSR 303 validator on a given domain object.
   * 
   * @param domainObject the domain object to be validated
   * @param <T> the type of the entity being validated
   * @return the violations associated with the domain object
   */
  @Override
  public <T> Set<ConstraintViolation<T>> validate(T domainObject) {
    return validator.validate(domainObject);
  }
}
