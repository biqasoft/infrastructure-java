/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.bpmn.safejs.internal;

@SuppressWarnings("all")
public class InterruptTest {
  public static boolean isInterrupted() {
    Thread _currentThread = Thread.currentThread();
    return _currentThread.isInterrupted();
  }
}
