package szoeke.bence.kafkaproducer.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkaproducer.entity.Event;
import szoeke.bence.kafkaproducer.utility.EventDeserializer;
import szoeke.bence.kafkaproducer.utility.EventSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyKafkaProducer {

    private static final String TOPIC_TO_SEND = "streams-input";
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);
    private static final String DELAY_ENV_VAR = "DELAY";
    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private final Properties properties;
    private final long delay;
    private final EventSerializer eventSerializer;
    private final EventDeserializer eventDeserializer;
    private List<Event> events;
    private long sequenceNumber = 0L;
    private StringSerializer stringSerializer;

    public MyKafkaProducer(ObjectMapper objectMapper) {
        this.stringSerializer = new StringSerializer();
        this.eventSerializer = new EventSerializer(objectMapper);
        this.eventDeserializer = new EventDeserializer(objectMapper);
        this.delay = Long.parseLong(System.getenv(DELAY_ENV_VAR));
        this.properties = new Properties();
        this.properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        this.properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        loadFilesFromDir();
    }

    public void produce() {
        try {
            produceMessages();
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while producing messages: %s", e.getMessage()));
        }
    }

    private void produceMessages() throws InterruptedException {
        while (true) {
            for (Event event : events) {
                Thread.sleep(delay);
                LOGGER.info("Waited " + delay + " MS =======================================================");
                sendDataToTopic(event);
            }
        }
    }

    private void sendDataToTopic(Event event) {
        try (KafkaProducer<String, Event> kafkaProducer = new KafkaProducer<>(properties, stringSerializer, eventSerializer)) {
            ProducerRecord<String, Event> record = new ProducerRecord<>(
                    TOPIC_TO_SEND,
                    String.valueOf(++sequenceNumber),
                    event);
            kafkaProducer.send(record);
            kafkaProducer.flush();
            LOGGER.info(String.format("File read successfully with sequenceNumber: %d", sequenceNumber));
        }
    }

    private void loadFilesFromDir() {
        try (Stream<Path> path = Files.walk(Paths.get("private_data/"))) {
            events = path
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(this::generateInputStream)
                    .filter(Objects::nonNull)
                    .map(this::mapInputStreamToEvent)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error(String.format("File read failed: %s", e.getMessage()));
        }
    }

    private InputStream generateInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("File not found: %s", e.getMessage()));
        }
        return null;
    }

    private Event mapInputStreamToEvent(InputStream inputStream) {
        try {
            return eventDeserializer.deserialize(null, inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Deserialization failed!", e);
        }
    }
}
