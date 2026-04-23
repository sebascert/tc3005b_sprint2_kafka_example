package com.example.producer.controller;

import com.example.producer.dto.PatientEvent;
import com.example.producer.service.KafkaProducerService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

  private final KafkaProducerService producer;

  @Value("${app.kafka.topics.patients}")
  private String patientsTopic;

  public PatientController(KafkaProducerService producer) {
    this.producer = producer;
  }

  @PostMapping
  public String createPatient(@RequestBody PatientEvent event) {
    event.setEventType("REGISTER");
    event.setTimestamp(LocalDateTime.now().toString());
    producer.sendMessage(patientsTopic, 0, event.getPatientId().toString(), event);
    return "Patient registration sent to Kafka";
  }

  @PutMapping("/{id}")
  public String updatePatient(@PathVariable Long id, @RequestBody PatientEvent event) {
    event.setPatientId(id);
    event.setEventType("UPDATE");
    event.setTimestamp(LocalDateTime.now().toString());
    producer.sendMessage(patientsTopic, 1, event.getPatientId().toString(), event);
    return "Patient update sent to Kafka";
  }

  @DeleteMapping("/{id}")
  public String deletePatient(@PathVariable Long id) {
    PatientEvent event = new PatientEvent();
    event.setEventType("DELETE");
    event.setPatientId(id);
    event.setTimestamp(LocalDateTime.now().toString());
    producer.sendMessage(patientsTopic, 2, id.toString(), event);
    return "Patient delete sent to Kafka";
  }
}
