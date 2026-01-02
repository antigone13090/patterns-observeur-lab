package com.acme.mlops.web.dto;

import com.acme.mlops.domain.ActionResult;
import com.acme.mlops.domain.MonitoringEvent;

import java.util.List;

public record EventResponse(MonitoringEvent event, List<ActionResult> actions) {}
