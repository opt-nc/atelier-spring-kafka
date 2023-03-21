package nc.opt.springkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nc.opt.springkafka.dto.MessageDTO;
import nc.opt.springkafka.dto.SmsDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        String json = "";
        try {
            json = objectMapper.writeValueAsString(messageDTO);
            String uuid = UUID.randomUUID().toString();

            final ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, uuid, json);
            return kafkaTemplate.send(record).get();

        } catch (ExecutionException e) {
            handleFailure(json, e.getCause());
        }
        catch (TimeoutException | InterruptedException e) {
            handleFailure(json, e.getCause());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Envoi asynchrone du message
    public void pushAsync(MessageDTO messageDTO) {

        try {
            final String json = objectMapper.writeValueAsString(messageDTO);
            String uuid = UUID.randomUUID().toString();

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(messageTopic, uuid, json);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    handleSuccess(json, result);
                }
                else {
                    handleFailure(json, ex.getCause());
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void handleSuccess(String data, SendResult<String, String> result){
        System.out.println("Sent message=[" + data +
                "] with offset=[" + result.getRecordMetadata().offset() + "]");
    }

    private void handleFailure(String data, Throwable ex){
        System.out.println("Unable to send message=["
                + data + "] due to : " + ex.getMessage());
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
