package nc.opt.springkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nc.opt.springkafka.dto.MessageDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(id = "messageListener", topics = "${opt.kafka.topics.message}", groupId = "messageG1")
    public void messageListener(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("Reception enregistrement brut de messageListener : [{}]", record);

        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO messageDTO = objectMapper.readValue(record.value(), MessageDTO.class);
        log.info("Reception message de messageListener : [{}]", messageDTO);

        latch.countDown();
    }

}
