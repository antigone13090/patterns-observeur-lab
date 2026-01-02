package com.acme.mlops.config;

import com.acme.mlops.observer.EventPublisher;
import com.acme.mlops.observer.impl.*;
import com.acme.mlops.service.MonitoringService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MlopsConfig {

  @Bean
  public EventPublisher eventPublisher() {
    EventPublisher p = new EventPublisher();
    p.subscribe(new DashboardUpdateObserver());
    p.subscribe(new SlackAlertObserver("#mlops-alerts"));
    p.subscribe(new EmailAlertObserver("mlops@acme.local"));
    p.subscribe(new TicketObserver("Jira/ML"));
    p.subscribe(new RetrainObserver());
    p.subscribe(new RollbackObserver());
    return p;
  }

  @Bean
  public MonitoringService monitoringService(EventPublisher publisher) {
    return new MonitoringService(publisher);
  }
}
