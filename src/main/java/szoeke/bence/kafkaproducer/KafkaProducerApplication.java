package szoeke.bence.kafkaproducer;

import szoeke.bence.kafkaproducer.producer.MyKafkaProducer;

public class KafkaProducerApplication {

    public static void main(String[] args) {
        MyKafkaProducer producer = new MyKafkaProducer();
        producer.produce();
    }
}
