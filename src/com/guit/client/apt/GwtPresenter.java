package com.guit.client.apt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as presenter.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GwtPresenter {

  String autofocus() default "";

  int autofocusDelay() default 0;

  String placeName() default "";

  boolean isWidget() default false;

  String title() default "";
}
