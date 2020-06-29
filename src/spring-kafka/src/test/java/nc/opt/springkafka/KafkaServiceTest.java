package nc.opt.springkafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;

@RunWith(SpringRunner.class)
@EmbeddedKafka(topics = {"${opt.kafka.topics.message}"})
@SpringBootTest(
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        classes = {KafkaService.class, KafkaAutoConfiguration.class})
public class KafkaServiceTest {

    @Value(value = "${opt.kafka.topics.message}")
    private String messageTopic;

    @Autowired
    private KafkaService kafkaService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private static final String AUTHOR = "Great Leader";
    private static final String BODY = "Dear %{Recipient}, foo, Kind Regards";
    private static final Long ID = 12345L;
    private static final String RECIPIENT = "World";
    private static final String SUBJECT = "Hello world";

    @Test
    public void push() {
        final Consumer<String, String> consumer = buildConsumer();

        MessageDTO m = createMessageDTO();
        SendResult<String, String> result = kafkaService.push(m);

        Assert.assertNotNull(result);

        String value = result.getProducerRecord().value();
        String key = result.getProducerRecord().key();

        embeddedKafka.consumeFromEmbeddedTopics(consumer, messageTopic);
        final ConsumerRecord<String, String> record = getSingleRecord(consumer, messageTopic, 10_000);

        assertThat(record, hasValue(value));
        assertThat(record, hasKey(key));

    }

    private <K, V> Consumer<K, V> buildConsumer() {
        final Map<String, Object> props = KafkaTestUtils.consumerProps("g1", "true", embeddedKafka);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        final DefaultKafkaConsumerFactory<K, V> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        return consumerFactory.createConsumer();
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
