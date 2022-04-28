package szoekebence.kafkaproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import szoekebence.kafkaproducer.producer.MyKafkaProducer;

@RestController
public class SendDataToTopicRestController {

    @Autowired
    private MyKafkaProducer producer;

    @PostMapping("/sendData")
    public void sendData() {
        producer.produce();
    }
}
