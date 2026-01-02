package com.acme.mlops.domain;

import java.time.Instant;

public record MonitoringEvent(
    String modelId,
    IncidentType incidentType,
    String metricName,
    double value,
    double threshold,
    Severity severity,
    String notes,
    Instant createdAt
) {}
