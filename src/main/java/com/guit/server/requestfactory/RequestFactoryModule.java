package com.guit.server.requestfactory;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public final class RequestFactoryModule extends ServletModule {

  private final String path;

  public RequestFactoryModule(String path) {
    this.path = path;
  }

  @Override
  protected void configureServlets() {
    serve(path).with(GuitRequestFactoryServlet.class);
  }

  @Provides
  @Singleton
  public ValidatorFactory getValidatorFactory(final Injector injector) {
    return Validation.byDefaultProvider().configure().constraintValidatorFactory(
        new ConstraintValidatorFactory() {
          @Override
          public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            return injector.getInstance(key);
          }
        }).buildValidatorFactory();
  }

  @Provides
  @Singleton
  public Validator getValidator(ValidatorFactory validatorFactory) {
    return validatorFactory.getValidator();
  }
}