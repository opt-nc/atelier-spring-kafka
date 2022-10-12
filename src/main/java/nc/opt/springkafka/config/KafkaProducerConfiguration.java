package nc.opt.springkafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

    @Bean
    public <T extends Object> ProducerFactory<String, T> producerFactory(ObjectMapper objectMapper, KafkaProperties properties) {
        return new DefaultKafkaProducerFactory<>(
            properties.buildProducerProperties(),
            new StringSerializer(),
            new JsonSerializer<>(objectMapper));
    }

}
