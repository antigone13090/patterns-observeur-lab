package com.acme.mlops.observer;

import com.acme.mlops.domain.ActionResult;
import com.acme.mlops.domain.MonitoringEvent;

public interface EventObserver {
  ActionResult onEvent(MonitoringEvent event);
}
