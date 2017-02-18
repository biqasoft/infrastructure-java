package com.biqasoft.common.watchablevalue;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate method in Spring and this method will be invoked with 1 param - new env value
 * Annotate property in Spring and this property will be updated with new env value
 *
 *
 * <example>
 *     @WatchableValue("biqa.internal.exception.strategy")
 *     public void onApplicationEvent(String newValue) {
 *      setSettings(newValue);
 *     }
 *
 *     @WatchableValue("biqa.internal.exception.strategy")
 *     private String strategy;
 * </example>
 *
 * Created by ya on 11/16/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface WatchableValue {

    @AliasFor("fieldName")
    String value() default "";

    @AliasFor("value")
    String fieldName() default "";

}
