package ru.dev.bachinin.kafka.properties;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Validated
public class KafkaProperties {

    @NotNull
    private List<String> bootstrapServers;

    @NotNull
    private String topic;

    private String groupId;
}
