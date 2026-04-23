package com.example.consumer.listener;

import com.example.consumer.dto.PatientStatusEvent;
import com.example.consumer.dto.PatientStatusResponse;
import com.example.consumer.service.StatusConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class StatusKafkaListener {

  private final StatusConsumerService statusConsumerService;

  public StatusKafkaListener(StatusConsumerService statusConsumerService) {
    this.statusConsumerService = statusConsumerService;
  }

  @KafkaListener(
      topicPartitions = {
        @TopicPartition(
            topic = "${app.kafka.topics.status}",
            partitions = {"0", "1"})
      },
      containerFactory = "statusKafkaListenerContainerFactory")
  @SendTo
  public PatientStatusResponse listen(PatientStatusEvent event) {
    return statusConsumerService.processStatusEvent(event);
  }
}
