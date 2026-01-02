package com.acme.mlops.domain;

public enum Severity {
  INFO, WARN, ALERT, CRITICAL;

  public boolean atLeast(Severity min) {
    return this.ordinal() >= min.ordinal();
  }
}
