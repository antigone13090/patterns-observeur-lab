package com.acme.mlops.service;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventPublisher;

import java.time.Instant;
import java.util.List;

public class MonitoringService {

  private final EventPublisher publisher;

  public MonitoringService(EventPublisher publisher) {
    this.publisher = publisher;
  }

  public MonitoringOutcome handle(MonitoringRequest req) {
    Severity severity = (req.forceSeverity() != null)
        ? req.forceSeverity()
        : SeverityRules.compute(req.incidentType(), req.value(), req.threshold());

    MonitoringEvent event = new MonitoringEvent(
        req.modelId(),
        req.incidentType(),
        req.metricName(),
        req.value(),
        req.threshold(),
        severity,
        req.notes(),
        Instant.now()
    );

    List<ActionResult> actions = publisher.publish(event);
    return new MonitoringOutcome(event, actions);
  }

  public record MonitoringRequest(
      String modelId,
      IncidentType incidentType,
      String metricName,
      double value,
      double threshold,
      String notes,
      Severity forceSeverity
  ) {}

  public record MonitoringOutcome(MonitoringEvent event, List<ActionResult> actions) {}
}
