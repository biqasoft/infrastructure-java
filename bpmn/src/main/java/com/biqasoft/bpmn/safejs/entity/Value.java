/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.bpmn.safejs.entity;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 2/16/2016.
 * All Rights Reserved
 */
public class Value<T> {

    private T value;

    public synchronized T get() {
        return value;
    }

    public synchronized Value<T> set(final T value) {
        this.value = value;
        return this;
    }

    public Value(final T value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "[(" + value + ") wrapped in (" + super.toString() + ")]";
    }

}
