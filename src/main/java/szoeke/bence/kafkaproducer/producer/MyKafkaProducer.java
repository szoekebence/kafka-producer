package szoeke.bence.kafkaproducer.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkaproducer.configuration.MyKafkaProducerConfiguration;
import szoeke.bence.kafkaproducer.utility.JsonNodeSerializer;

import java.util.List;

public class MyKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);
    private static final String TOPIC_TO_SEND = "streams-input";
    private static final String PRODUCING_TIME_MIN_ENV_VAR = "PRODUCING_TIME_MIN";
    private static final long PRODUCING_TIME_NANOS = Long.parseLong(System.getenv(PRODUCING_TIME_MIN_ENV_VAR)) * 60000000000L;
    private final KafkaProducer<String, JsonNode> kafkaProducer;
    private final List<JsonNode> events;

    public MyKafkaProducer(ObjectMapper objectMapper, MyKafkaProducerConfiguration configuration,
                           List<JsonNode> events) {
        this.kafkaProducer = new KafkaProducer<>(
                configuration.getProperties(),
                new StringSerializer(),
                new JsonNodeSerializer(objectMapper));
        this.events = events;
    }

    public void produce() {
        try (kafkaProducer) {
            sendEvents();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while producing messages: {}", e.getMessage());
        } finally {
            LOGGER.info("The producing has stopped after {} minutes.", System.getenv(PRODUCING_TIME_MIN_ENV_VAR));
        }
    }

    private void sendEvents() {
        long endTime = System.nanoTime() + PRODUCING_TIME_NANOS;
        while (System.nanoTime() < endTime) {
            events
                    .parallelStream()
                    .forEach(this::sendSingleEvent);
        }
    }

    private void sendSingleEvent(JsonNode event) {
        kafkaProducer.send(generateProducerRecord(event));
    }

    private ProducerRecord<String, JsonNode> generateProducerRecord(JsonNode event) {
        return new ProducerRecord<>(
                TOPIC_TO_SEND,
                event);
    }
}