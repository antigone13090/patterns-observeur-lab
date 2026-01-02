package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class DashboardUpdateObserver implements EventObserver {
  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    return ActionResult.ok(ActionType.UPDATE_DASHBOARD,
        "Dashboard updated: model=" + e.modelId() + ", incident=" + e.incidentType() + ", severity=" + e.severity());
  }
}
