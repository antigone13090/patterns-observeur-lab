package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class TicketObserver implements EventObserver {
  private final String project;
  public TicketObserver(String project) { this.project = project; }

  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    if (!e.severity().atLeast(Severity.ALERT)) return ActionResult.skipped(ActionType.OPEN_TICKET, "severity < ALERT");
    return ActionResult.ok(ActionType.OPEN_TICKET, "Ticket created in " + project + " : " + e.modelId() + " / " + e.incidentType());
  }
}
