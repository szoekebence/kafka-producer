package szoekebence.kafkaproducer;

import szoekebence.kafkaproducer.scheduler.MyProducer;

public class KafkaProducerApplication {

    public static void main(String[] args) {
        MyProducer producer = new MyProducer();
        producer.produce();
    }
}
