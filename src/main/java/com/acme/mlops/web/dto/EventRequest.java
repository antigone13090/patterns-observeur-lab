package com.acme.mlops.web.dto;

import com.acme.mlops.domain.IncidentType;
import com.acme.mlops.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
    @NotBlank String modelId,
    @NotNull IncidentType incidentType,
    @NotBlank String metricName,
    double value,
    double threshold,
    String notes,
    Severity forceSeverity
) {}
