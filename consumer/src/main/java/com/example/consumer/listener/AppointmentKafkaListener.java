package com.example.consumer.listener;

import com.example.consumer.dto.AppointmentEvent;
import com.example.consumer.service.AppointmentConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentKafkaListener {

  private final AppointmentConsumerService appointmentConsumerService;

  public AppointmentKafkaListener(AppointmentConsumerService appointmentConsumerService) {
    this.appointmentConsumerService = appointmentConsumerService;
  }

  @KafkaListener(
      topicPartitions = {
        @org.springframework.kafka.annotation.TopicPartition(
            topic = "appointments-topic",
            partitions = {"0", "1", "2"})
      },
      containerFactory = "appointmentKafkaListenerContainerFactory")
  public void listen(AppointmentEvent event) {
    appointmentConsumerService.processAppointmentEvent(event);
  }
}
