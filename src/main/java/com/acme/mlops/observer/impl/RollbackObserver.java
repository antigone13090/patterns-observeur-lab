package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class RollbackObserver implements EventObserver {
  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    boolean should =
        e.severity() == Severity.CRITICAL ||
        e.incidentType() == IncidentType.SECURITY ||
        (e.incidentType() == IncidentType.ERROR_RATE && e.severity().atLeast(Severity.ALERT));
    if (!should) return ActionResult.skipped(ActionType.ROLLBACK, "conditions not met");
    return ActionResult.ok(ActionType.ROLLBACK, "Rollback triggered for model=" + e.modelId() + " (reason=" + e.incidentType() + ")");
  }
}
