package com.example.producer.config;

import com.example.producer.dto.PatientStatusEvent;
import com.example.producer.dto.PatientStatusResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  private static final String REPLY_TOPIC = "patient-status-reply-topic";
  private static final String REPLY_GROUP = "producer-reply-group";

  @Bean
  public ProducerFactory<String, PatientStatusEvent> statusProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public ConsumerFactory<String, PatientStatusResponse> replyConsumerFactory() {
    JsonDeserializer<PatientStatusResponse> deserializer =
        new JsonDeserializer<>(PatientStatusResponse.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, REPLY_GROUP);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PatientStatusResponse>
      replyListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PatientStatusResponse> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(replyConsumerFactory());
    return factory;
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, PatientStatusResponse> repliesContainer() {
    ConcurrentMessageListenerContainer<String, PatientStatusResponse> container =
        replyListenerContainerFactory().createContainer(REPLY_TOPIC);
    container.getContainerProperties().setGroupId(REPLY_GROUP);
    container.setAutoStartup(false);
    return container;
  }

  @Bean
  public ReplyingKafkaTemplate<String, PatientStatusEvent, PatientStatusResponse>
      replyingKafkaTemplate() {
    ReplyingKafkaTemplate<String, PatientStatusEvent, PatientStatusResponse> template =
        new ReplyingKafkaTemplate<>(statusProducerFactory(), repliesContainer());
    template.setDefaultReplyTimeout(Duration.ofSeconds(10));
    return template;
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
  }
}
