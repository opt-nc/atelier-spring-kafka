package nc.opt.springkafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaService {

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @Value(value = "${opt.kafka.topics.message}")
    private String messageTopic;

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public SendResult<String, String> push(MessageDTO messageDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(messageDTO);
            String uuid = UUID.randomUUID().toString();

            ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, uuid, json);
            return kafkaTemplate.send(record).get();

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi dans kafka de [{}]", messageDTO);
            throw new RuntimeException(e);
        }
    }
}
