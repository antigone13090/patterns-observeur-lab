package com.acme.mlops.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoringControllerIT {

  @Autowired MockMvc mvc;

  @Test
  void alertTriggersMultipleActions() throws Exception {
    String body =
        "{"
            + "\"modelId\":\"reco-v3\","
            + "\"incidentType\":\"DRIFT\","
            + "\"metricName\":\"psi_drift\","
            + "\"value\":0.35,"
            + "\"threshold\":0.20,"
            + "\"forceSeverity\":\"ALERT\","
            + "\"notes\":\"batch=2026-01-01\""
        + "}";

    mvc.perform(post("/api/mlops/event")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.event.modelId").value("reco-v3"))
        .andExpect(jsonPath("$.event.severity").value("ALERT"))
        .andExpect(jsonPath("$.actions", hasSize(greaterThanOrEqualTo(3))))
        .andExpect(jsonPath("$.actions[*].type", hasItem("SLACK_ALERT")))
        .andExpect(jsonPath("$.actions[*].type", hasItem("OPEN_TICKET")))
        .andExpect(jsonPath("$.actions[*].type", hasItem("UPDATE_DASHBOARD")));
  }
}
