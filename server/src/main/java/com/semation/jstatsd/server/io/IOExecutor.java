package com.semation.jstatsd.server.io;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 18:24
 */
@BindingAnnotation
@Target(value = {FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface IOExecutor {
}
