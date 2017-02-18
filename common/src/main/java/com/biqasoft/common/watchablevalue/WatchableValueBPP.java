package com.biqasoft.common.watchablevalue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ya on 11/16/2016.
 */
@Component
public class WatchableValueBPP implements BeanPostProcessor, ApplicationListener<EnvironmentChangeEvent> {

    private List<WatchableInvokableMethods> invokableMethods = new ArrayList<>();
    private List<WatchableField> watchableField = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(WatchableValueBPP.class);
    private final Environment environment;

    @Autowired
    public WatchableValueBPP(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class<?> aClass = o.getClass();

        // find fields, annotated with @WatchableValue
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            Annotation annotation = AnnotationUtils.findAnnotation(field, WatchableValue.class);
            if (annotation != null) {
                watchableField.add(new WatchableField(o, field, (String) AnnotationUtils.getValue(annotation, "value")));
            }
        }

        // find methods, annotated with @WatchableValue
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            Annotation annotation = AnnotationUtils.findAnnotation(method, WatchableValue.class);
            if (annotation != null) {
                invokableMethods.add(new WatchableInvokableMethods(o, method, (String) AnnotationUtils.getValue(annotation, "value")));
            }
        }

        return o;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent environmentChangeEvent) {
        Set<String> keys = environmentChangeEvent.getKeys();

        for (String key : keys) {

            // invoke method
            for (WatchableInvokableMethods methods : invokableMethods) {
                if (methods.variable.equals(key)) {
                    try {
                        methods.method.invoke(methods.object, environment.getProperty(key));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.warn("Error watchable method {} update", key, e);
                    }
                }
            }

            // update fields
            for (WatchableField field : watchableField) {
                if (field.variable.equals(key)) {
                    field.field.setAccessible(true);
                    try {
                        field.field.set(field.object, environment.getProperty(key));
                    } catch (IllegalAccessException e) {
                        logger.warn("Error watchable field {} update", key, e);
                    }
                }
            }

        }
    }
}

class WatchableInvokableMethods {

    Object object;
    Method method;
    String variable;

    WatchableInvokableMethods(Object object, Method method, String variable) {
        this.object = object;
        this.method = method;
        this.variable = variable;
    }
}

class WatchableField {

    Object object;
    Field field;
    String variable;

    WatchableField(Object object, Field field, String variable) {
        this.object = object;
        this.field = field;
        this.variable = variable;
    }
}
