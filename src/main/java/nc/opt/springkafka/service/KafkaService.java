package nc.opt.springkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nc.opt.springkafka.dto.MessageDTO;
import nc.opt.springkafka.dto.SmsDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
public class KafkaService {

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @Value(value = "${opt.kafka.topics.message}")
    private String messageTopic;

    @Value(value = "${opt.kafka.topics.sms}")
    private String smsTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Envoi synchrone du message
    // Le producer attend la réponse de Kafka
    public SendResult<String, String> push(MessageDTO messageDTO) {
        try {
            String json = objectMapper.writeValueAsString(messageDTO);
            String uuid = UUID.randomUUID().toString();

            ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, uuid, json);
            return kafkaTemplate.send(record).get();

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi dans kafka de [{}]", messageDTO);
            throw new RuntimeException(e);
        }
    }

    // Envoi asynchrone du message
    public void pushAsync(MessageDTO messageDTO) {

        String json = null;
        try {
            json = objectMapper.writeValueAsString(messageDTO);

            String uuid = UUID.randomUUID().toString();

            ListenableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(messageTopic, json);

            String finalJson = json;
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Sent message=[" + finalJson +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }
                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=["
                            + finalJson + "] due to : " + ex.getMessage());
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public SendResult<String, String> pushSms(SmsDTO smsDTO) {
        try {
            String json = objectMapper.writeValueAsString(smsDTO);
            String key = smsDTO.getPhoneNumberEmitter();

            ProducerRecord<String, String> record = new ProducerRecord<>(smsTopic, key, json);
            return kafkaTemplate.send(record).get();

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de SMS dans kafka de [{}]", smsDTO);
            throw new RuntimeException(e);
        }
    }
}
