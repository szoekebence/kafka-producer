package szoeke.bence.kafkaproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import szoeke.bence.kafkaproducer.configuration.MyKafkaProducerConfiguration;
import szoeke.bence.kafkaproducer.producer.MyKafkaProducer;
import szoeke.bence.kafkaproducer.utility.FileParser;

public class KafkaProducerApplication {

    public static void main(String[] args) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
        MyKafkaProducer producer = new MyKafkaProducer(
                objectMapper,
                new MyKafkaProducerConfiguration(),
                new FileParser(objectMapper).generateEventsFromFiles());
        producer.produce();
    }
}
