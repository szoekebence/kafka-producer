package szoekebence.kafkaproducer.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    private final Properties properties;
    private List<InputStream> inputStreams;
    private Integer sequenceNumber = 0;

    public MyKafkaProducer() {
        loadFilesFromDir();
        properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    public void produce() {
        try {
            produceMessages();
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while producing messages: %s", e.getMessage()));
        }
    }

    private void produceMessages() throws IOException, InterruptedException {
        for (InputStream inputStream : inputStreams) {
            sendDataToTopic(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
            Thread.sleep(500);
        }
    }

    private void sendDataToTopic(String data) {
        try (KafkaProducer<Void, String> kafkaProducer = new KafkaProducer<>(properties)) {
            ProducerRecord<Void, String> record = new ProducerRecord<>(TOPIC_TO_SEND, data);
            kafkaProducer.send(record);
            kafkaProducer.flush();
            LOGGER.info(String.format("File read successfully with sequenceNumber: %d", sequenceNumber++));
        }
    }

    private void loadFilesFromDir() {
        try (Stream<Path> path = Files.walk(Paths.get("src/main/resources/private_data/custom/"))) {
            inputStreams = path
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(this::generateInputStream)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.warn(String.format("File read failed: %s", e.getMessage()));
        }
    }

    private InputStream generateInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOGGER.warn(String.format("File not found: %s", e.getMessage()));
        }
        return null;
    }
}
