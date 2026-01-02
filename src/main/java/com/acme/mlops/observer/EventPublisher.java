package com.acme.mlops.observer;

import com.acme.mlops.domain.ActionResult;
import com.acme.mlops.domain.MonitoringEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventPublisher {
  private final List<EventObserver> observers = new ArrayList<>();

  public void subscribe(EventObserver o) { observers.add(o); }
  public void unsubscribe(EventObserver o) { observers.remove(o); }

  public List<ActionResult> publish(MonitoringEvent event) {
    if (observers.isEmpty()) return List.of();
    List<ActionResult> out = new ArrayList<>(observers.size());
    for (EventObserver o : observers) out.add(o.onEvent(event));
    return Collections.unmodifiableList(out);
  }
}
