package com.example.consumer.listener;

import com.example.consumer.dto.PatientStatusEvent;
import com.example.consumer.service.StatusConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StatusKafkaListener {

  private final StatusConsumerService statusConsumerService;

  public StatusKafkaListener(StatusConsumerService statusConsumerService) {
    this.statusConsumerService = statusConsumerService;
  }

  @KafkaListener(
      topicPartitions = {
        @org.springframework.kafka.annotation.TopicPartition(
            topic = "patient-status-topic",
            partitions = {"0", "1"})
      },
      containerFactory = "statusKafkaListenerContainerFactory")
  public void listen(PatientStatusEvent event) {
    statusConsumerService.processStatusEvent(event);
  }
}
