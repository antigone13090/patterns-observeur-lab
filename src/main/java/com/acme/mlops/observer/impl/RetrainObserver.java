package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class RetrainObserver implements EventObserver {
  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    boolean should = e.incidentType() == IncidentType.DRIFT && e.severity().atLeast(Severity.WARN);
    if (!should) return ActionResult.skipped(ActionType.RETRAIN, "only DRIFT + severity>=WARN");
    return ActionResult.ok(ActionType.RETRAIN, "Retrain scheduled for model=" + e.modelId() + " (metric=" + e.metricName() + ")");
  }
}
