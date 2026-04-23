package com.example.producer.controller;

import com.example.producer.dto.PatientStatusEvent;
import com.example.producer.dto.PatientStatusResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients/{id}")
public class StatusController {

  private final ReplyingKafkaTemplate<String, PatientStatusEvent, PatientStatusResponse>
      replyingTemplate;

  public StatusController(
      ReplyingKafkaTemplate<String, PatientStatusEvent, PatientStatusResponse> replyingTemplate) {
    this.replyingTemplate = replyingTemplate;
  }

  @GetMapping("/status")
  public ResponseEntity<?> queryStatus(@PathVariable Long id) {
    PatientStatusEvent event = new PatientStatusEvent();
    event.setPatientId(id);
    event.setEventType("STATUS_QUERY");
    event.setTimestamp(LocalDateTime.now().toString());

    return sendAndReceive(event, 0);
  }

  @GetMapping("/history")
  public ResponseEntity<?> queryHistory(@PathVariable Long id) {
    PatientStatusEvent event = new PatientStatusEvent();
    event.setPatientId(id);
    event.setEventType("HISTORY_QUERY");
    event.setTimestamp(LocalDateTime.now().toString());

    return sendAndReceive(event, 1);
  }

  private ResponseEntity<?> sendAndReceive(PatientStatusEvent event, int partition) {
    try {
      ProducerRecord<String, PatientStatusEvent> record =
          new ProducerRecord<>(
              "patient-status-topic", partition, event.getPatientId().toString(), event);

      record
          .headers()
          .add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "patient-status-reply-topic".getBytes()));

      RequestReplyFuture<String, PatientStatusEvent, PatientStatusResponse> future =
          replyingTemplate.sendAndReceive(record);

      PatientStatusResponse response = future.get(10, TimeUnit.SECONDS).value();
      return ResponseEntity.ok(response);

    } catch (TimeoutException e) {
      return ResponseEntity.status(504).body("Consumer did not reply in time");
    } catch (InterruptedException | ExecutionException e) {
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }
}
