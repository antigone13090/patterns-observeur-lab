package com.acme.mlops.web;

import com.acme.mlops.service.MonitoringService;
import com.acme.mlops.web.dto.EventRequest;
import com.acme.mlops.web.dto.EventResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MonitoringController {

  private final MonitoringService service;

  public MonitoringController(MonitoringService service) {
    this.service = service;
  }

  @GetMapping("/health")
  public Map<String, Object> health() {
    return Map.of("ok", true, "service", "mlops-observer-lab");
  }

  @PostMapping("/api/mlops/event")
  public EventResponse publish(@Valid @RequestBody EventRequest req) {
    var out = service.handle(new MonitoringService.MonitoringRequest(
        req.modelId(),
        req.incidentType(),
        req.metricName(),
        req.value(),
        req.threshold(),
        req.notes(),
        req.forceSeverity()
    ));
    return new EventResponse(out.event(), out.actions());
  }
}
