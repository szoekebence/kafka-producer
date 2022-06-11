package szoeke.bence.kafkaproducer.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkaproducer.configuration.MyKafkaProducerConfiguration;
import szoeke.bence.kafkaproducer.utility.JsonNodeSerializer;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class MyKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);
    private static final String TOPIC_TO_SEND = "streams-input";
    private static final String DELAY_ENV_VAR = "DELAY_MS";
    private static final long TEN_MINUTES_IN_NANOS = 600000000000L;
    private static final long DELAY = Long.parseLong(System.getenv(DELAY_ENV_VAR));
    private final KafkaProducer<Void, JsonNode> kafkaProducer;
    private final List<ProducerRecord<Void, JsonNode>> producerRecords;
    private long sequenceNumber = 0L;

    public MyKafkaProducer(ObjectMapper objectMapper, MyKafkaProducerConfiguration configuration,
                           List<JsonNode> events) {
        this.kafkaProducer = new KafkaProducer<>(
                configuration.getProperties(),
                new VoidSerializer(),
                new JsonNodeSerializer(objectMapper));
        this.producerRecords = generateProducerRecords(events);
        LOGGER.info(String.format("%d ms delay set between each event.\n", DELAY));
    }

    public void produce() {
        try (kafkaProducer) {
            sendEventsToTopic();
            LOGGER.info(String.format("The producing has stopped after 10 minutes and %d events.", sequenceNumber));
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while producing messages: %s", e.getMessage()));
        } finally {
            kafkaProducer.flush();
            kafkaProducer.close(Duration.ofSeconds(10L));
        }
    }

    private void sendEventsToTopic() {
        long endTime = System.nanoTime() + TEN_MINUTES_IN_NANOS;
        while (System.nanoTime() < endTime) {
            producerRecords
                    .parallelStream()
                    .forEach(this::sendSingleEventToTopic);
        }
    }

    private void sendSingleEventToTopic(ProducerRecord<Void, JsonNode> producerRecord) {
        sleepIfNeeded();
        kafkaProducer.send(producerRecord);
        sequenceNumber++;
    }

    private List<ProducerRecord<Void, JsonNode>> generateProducerRecords(List<JsonNode> events) {
        return events
                .stream()
                .map(this::generateSingleProducerRecord)
                .collect(Collectors.toList());
    }

    private ProducerRecord<Void, JsonNode> generateSingleProducerRecord(JsonNode event) {
        return new ProducerRecord<>(
                TOPIC_TO_SEND,
                event);
    }

    private void sleepIfNeeded() {
        if (DELAY > 0) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                LOGGER.error("Exception occurred by sleep for delay: ", e);
            }
        }
    }
}
