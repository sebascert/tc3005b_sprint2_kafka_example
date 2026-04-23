package com.example.producer.controller;

import com.example.producer.dto.AppointmentEvent;
import com.example.producer.service.KafkaProducerService;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

  private final KafkaProducerService producer;

  public AppointmentController(KafkaProducerService producer) {
    this.producer = producer;
  }

  @PostMapping
  public String createAppointment(@RequestBody AppointmentEvent event) {
    event.setEventType("CREATE");
    event.setTimestamp(LocalDateTime.now().toString());

    producer.sendMessage("appointments-topic", 0, event.getPatientId().toString(), event);

    return "Appointment created event sent";
  }

  @DeleteMapping("/{id}")
  public String cancelAppointment(@PathVariable Long id, @RequestBody AppointmentEvent event) {
    event.setAppointmentId(id);
    event.setEventType("CANCEL");
    event.setTimestamp(LocalDateTime.now().toString());

    producer.sendMessage("appointments-topic", 1, event.getPatientId().toString(), event);

    return "Appointment cancel event sent";
  }

  @PutMapping("/{id}/reschedule")
  public String rescheduleAppointment(@PathVariable Long id, @RequestBody AppointmentEvent event) {
    event.setAppointmentId(id);
    event.setEventType("RESCHEDULE");
    event.setTimestamp(LocalDateTime.now().toString());

    producer.sendMessage("appointments-topic", 2, event.getPatientId().toString(), event);

    return "Appointment reschedule event sent";
  }
}
