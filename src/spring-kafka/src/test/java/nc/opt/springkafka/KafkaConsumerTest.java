package nc.opt.springkafka;

import nc.opt.springkafka.consumer.KafkaConsumer;
import nc.opt.springkafka.dto.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@EmbeddedKafka(topics = {"${opt.kafka.topics.message}"})
@SpringBootTest(
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        classes = {KafkaConsumer.class, KafkaAutoConfiguration.class})
public class KafkaConsumerTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Value(value = "${opt.kafka.topics.message}")
    private String messageTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private static final String AUTHOR = "Great Leader";
    private static final String BODY = "Dear %{Recipient}, foo, Kind Regards";
    private static final Long ID = 12345L;
    private static final String RECIPIENT = "World";
    private static final String SUBJECT = "Hello world";
    private static final String UID = UUID.randomUUID().toString();

    @Test
    public void receive() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate = new KafkaTemplate<>(buildProducer());
        kafkaTemplate.setDefaultTopic(messageTopic);
        MessageDTO m = createMessageDTO();
        String json = objectMapper.writeValueAsString(m);

        ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, UID, json);

        kafkaTemplate.send(record);

        kafkaConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(kafkaConsumer.getLatch().getCount()).isEqualTo(0);

    }



    private ProducerFactory<String, String> buildProducer() {
        final Map<String, Object> props = KafkaTestUtils.producerProps(embeddedKafka.getBrokersAsString());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    private MessageDTO createMessageDTO() {
        MessageDTO m = new MessageDTO();
        m.setAuthor(AUTHOR);
        m.setBody(BODY);
        m.setId(ID);
        m.setRecipient(RECIPIENT);
        m.setSubject(SUBJECT);
        return m;
    }

}

