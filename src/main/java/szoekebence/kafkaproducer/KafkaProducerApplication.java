package szoekebence.kafkaproducer;

import szoekebence.kafkaproducer.scheduler.MyKafkaProducer;

public class KafkaProducerApplication {

    public static void main(String[] args) {
        MyKafkaProducer producer = new MyKafkaProducer();
        producer.produce();
    }
}
