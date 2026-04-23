package com.example.consumer.config;

import com.example.consumer.dto.AppointmentEvent;
import com.example.consumer.dto.PatientEvent;
import com.example.consumer.dto.PatientStatusEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  private final KafkaProperties kafkaProperties;

  public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  private Map<String, Object> baseConsumerProps() {
    Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return props;
  }

  @Bean
  public ConsumerFactory<String, PatientEvent> patientEventConsumerFactory() {
    JsonDeserializer<PatientEvent> deserializer = new JsonDeserializer<>(PatientEvent.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);
    return new DefaultKafkaConsumerFactory<>(
        baseConsumerProps(), new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PatientEvent>
      patientKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PatientEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(patientEventConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, AppointmentEvent> appointmentEventConsumerFactory() {
    JsonDeserializer<AppointmentEvent> deserializer =
        new JsonDeserializer<>(AppointmentEvent.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);
    return new DefaultKafkaConsumerFactory<>(
        baseConsumerProps(), new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, AppointmentEvent>
      appointmentKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, AppointmentEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(appointmentEventConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, PatientStatusEvent> patientStatusEventConsumerFactory() {
    JsonDeserializer<PatientStatusEvent> deserializer =
        new JsonDeserializer<>(PatientStatusEvent.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);
    return new DefaultKafkaConsumerFactory<>(
        baseConsumerProps(), new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PatientStatusEvent>
      statusKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PatientStatusEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(patientStatusEventConsumerFactory());
    return factory;
  }
}
