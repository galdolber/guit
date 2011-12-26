package com.guit.client;

/**
 * This annotation is used to configure the view of a presenter.
 */
public @interface ViewProperties {

  /**
   * Defines how we create the view.
   */
  ViewType type() default ViewType.COMPOSITE;

  /**
   * Specify a custom template.
   */
  String template() default "";
}
