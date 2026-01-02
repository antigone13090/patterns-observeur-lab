package com.acme.mlops.observer.impl;

import com.acme.mlops.domain.*;
import com.acme.mlops.observer.EventObserver;

public class SlackAlertObserver implements EventObserver {
  private final String channel;
  public SlackAlertObserver(String channel) { this.channel = channel; }

  @Override
  public ActionResult onEvent(MonitoringEvent e) {
    if (!e.severity().atLeast(Severity.ALERT)) return ActionResult.skipped(ActionType.SLACK_ALERT, "severity < ALERT");
    String msg = "Slack -> " + channel + " : [" + e.severity() + "] " + e.modelId() + " / " + e.incidentType()
        + " metric=" + e.metricName() + " value=" + e.value() + " thr=" + e.threshold();
    return ActionResult.ok(ActionType.SLACK_ALERT, msg);
  }
}
