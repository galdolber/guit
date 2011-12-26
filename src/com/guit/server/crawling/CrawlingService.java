package com.guit.server.crawling;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Binding annotation to specify the location of the crawling service.
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface CrawlingService {
}
