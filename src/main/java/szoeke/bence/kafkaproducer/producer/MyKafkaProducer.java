package szoeke.bence.kafkaproducer.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkaproducer.configuration.MyKafkaProducerConfiguration;
import szoeke.bence.kafkaproducer.entity.Event;
import szoeke.bence.kafkaproducer.utility.EventSerializer;

import java.util.List;

public class MyKafkaProducer {

    private static final String TOPIC_TO_SEND = "streams-input";
    private static final long TEN_MINUTES_IN_NANOS = 600000000000L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);
    private final KafkaProducer<String, Event> kafkaProducer;
    private final List<Event> events;
    private long sequenceNumber = 0L;

    public MyKafkaProducer(ObjectMapper objectMapper, MyKafkaProducerConfiguration configuration,
                           List<Event> events) {
        this.kafkaProducer = new KafkaProducer<>(
                configuration.getProperties(),
                new StringSerializer(),
                new EventSerializer(objectMapper));
        this.events = events;
    }

    public void produce() {
        try (kafkaProducer) {
            sendEventsToTopic();
            LOGGER.info(String.format("The producing has stopped after 10 minutes and %d events.", sequenceNumber));
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while producing messages: %s", e.getMessage()));
        }
    }

    private void sendEventsToTopic() {
        long endTime = System.nanoTime() + TEN_MINUTES_IN_NANOS;
        while (System.nanoTime() < endTime) {
            events
                    .parallelStream()
                    .forEach(this::sendSingleEventToTopic);
        }
    }

    private void sendSingleEventToTopic(Event event) {
        ProducerRecord<String, Event> record = new ProducerRecord<>(
                TOPIC_TO_SEND,
                String.valueOf(++sequenceNumber),
                event);
        kafkaProducer.send(record);
        kafkaProducer.flush();
    }
}
