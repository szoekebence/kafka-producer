package szoeke.bence.kafkaproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaproducer.producer.MyKafkaProducer;

public class KafkaProducerApplication {

    public static void main(String[] args) {
        MyKafkaProducer producer = new MyKafkaProducer(new ObjectMapper());
        producer.produce();
    }
}
