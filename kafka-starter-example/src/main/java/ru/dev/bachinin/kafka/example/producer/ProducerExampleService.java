package ru.dev.bachinin.kafka.example.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.dev.bachinin.kafka.properties.KafkaProperties;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerExampleService implements CommandLineRunner {

    private final Producer<String, String> kafkaProducer;
    private final KafkaProperties producerProperties;

    @Override
    public void run(String... args) {
        String topic = producerProperties.getTopic();
        try (kafkaProducer) {
            while (true) {
                String key = UUID.randomUUID().toString();
                String value = RandomStringUtils.randomAlphabetic(20);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(4000));
                kafkaProducer.send(new ProducerRecord<>(topic, key, value),
                        (event, ex) -> {
                            if (ex != null)
                                log.error(ex.getMessage());
                            else
                                log.debug("Produced event to topic '{}': key = '{}' value = '{}'", topic, key, value);
                        });
            }
        }

    }
}
