package nc.opt.springkafka.stream;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaStream {

    private final Logger log = LoggerFactory.getLogger(KafkaStream.class);

    @Value(value = "${opt.kafka.topics.sms}")
    private String smsTopic;

    @Value(value = "${opt.kafka.topics.user}")
    private String userTopic;

    @Bean
    public KStream<String, String> smsStream(StreamsBuilder kStreamBuilder) {
        KStream<String, String> smsKStream = kStreamBuilder.stream(smsTopic);
        KTable<String, String> userKTable = kStreamBuilder.table(userTopic);

        KStream<String, String> finalStream = smsKStream
                                                .leftJoin(userKTable,
                                                    (value1, value2) -> value1 + " " + value2
                                                );

        finalStream.print(Printed.toSysOut());

        return finalStream;
    }

}
