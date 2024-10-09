package ru.dev.bachinin.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import ru.dev.bachinin.kafka.config.KafkaAutoConfig;
import ru.dev.bachinin.kafka.properties.KafkaProperties;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@Import(KafkaAutoConfig.class)
public class KafkaAutoConfigTest {

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:latest");

    @Configuration
    static class TestConfig {
        @Bean
        public KafkaProperties producerProperties() {
            KafkaProperties properties = new KafkaProperties();
            properties.setBootstrapServers(kafkaContainer.getBootstrapServers());
            properties.setTopic("test-topic");
            return properties;
        }

        @Bean
        public KafkaProperties consumerProperties() {
            KafkaProperties properties = new KafkaProperties();
            properties.setBootstrapServers(kafkaContainer.getBootstrapServers());
            properties.setGroupId("test-group");
            properties.setTopic("test-topic");
            return properties;
        }
    }

    @Autowired
    private Consumer<String, String> kafkaConsumer;

    @Autowired
    private Producer<String, String> kafkaProducer;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testKafkaConsumerBean() {
        assertNotNull(kafkaConsumer, "Kafka Consumer should not be null");
    }

    @Test
    void testKafkaProducerBean() {
        assertNotNull(kafkaProducer, "Kafka Producer should not be null");
    }

    @Test
    public void testKafkaProducerAndConsumer() {

        KafkaProperties producerProperties = applicationContext.getBean("producerProperties", KafkaProperties.class);
        KafkaProperties consumerProperties = applicationContext.getBean("consumerProperties", KafkaProperties.class);

        kafkaConsumer.subscribe(Collections.singletonList(consumerProperties.getTopic()));
        kafkaProducer.send(new ProducerRecord<>(producerProperties.getTopic(), "key", "value"));

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

        records.forEach(record -> {
            assertThat(record.key()).isEqualTo("key");
            assertThat(record.value()).isEqualTo("value");
        });

    }
}
