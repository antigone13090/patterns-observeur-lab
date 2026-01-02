package com.acme.mlops.domain;

public record ActionResult(ActionType type, String status, String message) {
  public static ActionResult ok(ActionType type, String message) {
    return new ActionResult(type, "OK", message);
  }
  public static ActionResult skipped(ActionType type, String message) {
    return new ActionResult(type, "SKIPPED", message);
  }
}
