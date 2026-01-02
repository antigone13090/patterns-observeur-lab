package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class EmailAlertObserver implements EventObserver {
  private final String to;
  public EmailAlertObserver(String to) { this.to = to; }

  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    if (!e.severity().atLeast(Severity.WARN)) return ActionResult.skipped(ActionType.EMAIL_ALERT, "severity < WARN");
    String msg = "Email -> " + to + " : [" + e.severity() + "] " + e.modelId() + " / " + e.incidentType()
        + " metric=" + e.metricName() + " value=" + e.value() + " thr=" + e.threshold();
    return ActionResult.ok(ActionType.EMAIL_ALERT, msg);
  }
}
