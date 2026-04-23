package com.example.consumer.listener;

import com.example.consumer.dto.PatientEvent;
import com.example.consumer.service.PatientConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PatientKafkaListener {

  private final PatientConsumerService patientConsumerService;

  public PatientKafkaListener(PatientConsumerService patientConsumerService) {
    this.patientConsumerService = patientConsumerService;
  }

  @KafkaListener(
      topicPartitions = {
        @org.springframework.kafka.annotation.TopicPartition(
            topic = "${app.kafka.topics.patients}",
            partitions = {"0", "1", "2"})
      },
      containerFactory = "patientKafkaListenerContainerFactory")
  public void listen(PatientEvent event) {
    patientConsumerService.processPatientEvent(event);
  }
}
