package ru.dev.bachinin.kafka.example.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.dev.bachinin.kafka.properties.KafkaProperties;

import java.time.Duration;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerExampleService implements CommandLineRunner {

    private final Consumer<String, String> kafkaConsumer;
    private final KafkaProperties consumerProperties;

    @Override
    public void run(String... args) {
        try (kafkaConsumer) {
            kafkaConsumer.subscribe(Collections.singletonList(consumerProperties.getTopic()));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    log.debug("Consumed event from topic '{}': key = '{}', value = '{}'", consumerProperties.getTopic(), key, value);
                }
            }
        }
    }
}
