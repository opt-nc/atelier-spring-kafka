package nc.opt.springkafka.web.rest;

import nc.opt.springkafka.dto.SmsDTO;
import nc.opt.springkafka.service.KafkaService;
import nc.opt.springkafka.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaResource {

    private final Logger log = LoggerFactory.getLogger(KafkaResource.class);

    private KafkaService kafkaService;

    public KafkaResource(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("/send")
    public ResponseEntity sendKafka(@RequestBody MessageDTO messageDTO) {
        log.info("Requete REST pour envoyer un message dans Kafka : [{}]", messageDTO);

        SendResult<String, String> result = kafkaService.push(messageDTO);
        return ResponseEntity.ok().body(result.toString());
    }

    @PostMapping("/sendAsync")
    public void sendKafkaAsync(@RequestBody MessageDTO messageDTO) {
        log.info("Requete REST pour envoyer un message en asynchrone dans Kafka : [{}]", messageDTO);

        kafkaService.push(messageDTO);
    }

    @PostMapping("/sms/send")
    public ResponseEntity sendSms(@RequestBody SmsDTO smsDTO) {
        log.info("Requete REST pour envoyer un sms dans Kafka : [{}]", smsDTO);

        SendResult<String, String> result = kafkaService.pushSms(smsDTO);
        return ResponseEntity.ok().body(result.toString());
    }
}
