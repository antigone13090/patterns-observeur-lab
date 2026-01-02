package com.acme.mlops.service;

import com.acme.mlops.domain.IncidentType;
import com.acme.mlops.domain.Severity;

public final class SeverityRules {
  private SeverityRules() {}

  public static Severity compute(IncidentType type, double value, double threshold) {
    if (threshold <= 0.0) return Severity.WARN;
    if (value <= threshold) return Severity.INFO;

    double ratio = value / threshold;
    if (ratio <= 1.25) return Severity.WARN;
    if (ratio <= 1.75) return Severity.ALERT;
    return Severity.CRITICAL;
  }
}
