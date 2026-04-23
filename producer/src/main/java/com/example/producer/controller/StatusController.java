package com.example.producer.controller;

import com.example.producer.dto.PatientStatusEvent;
import com.example.producer.service.KafkaProducerService;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients/{id}")
public class StatusController {

  private final KafkaProducerService producer;

  public StatusController(KafkaProducerService producer) {
    this.producer = producer;
  }

  @PostMapping("/status-queries")
  public String queryStatus(@PathVariable Long id, @RequestBody PatientStatusEvent event) {
    event.setPatientId(id);
    event.setEventType("STATUS_QUERY");
    event.setTimestamp(LocalDateTime.now().toString());

    producer.sendMessage("patient-status-topic", 0, event.getPatientId().toString(), event);

    return "Status query event sent";
  }

  @PostMapping("/history-queries")
  public String queryHistory(@PathVariable Long id, @RequestBody PatientStatusEvent event) {
    event.setPatientId(id);
    event.setEventType("HISTORY_QUERY");
    event.setTimestamp(LocalDateTime.now().toString());

    producer.sendMessage("patient-status-topic", 1, event.getPatientId().toString(), event);

    return "History query event sent";
  }
}
