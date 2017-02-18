/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.bpmn.safejs.internal;

import jdk.nashorn.api.scripting.ClassFilter;

import java.util.Set;

@SuppressWarnings("all")
public class SandboxClassFilter implements ClassFilter {
  private final Set<String> allowed;
  
  @Override
  public boolean exposeToScripts(final String className) {
    return this.allowed.contains(className);
  }
  
  public SandboxClassFilter(final Set<String> allowed) {
    this.allowed = allowed;
  }
}
