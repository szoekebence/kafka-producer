package szoekebence.kafkaproducer.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
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
    private static final String DELAY_ENV_VAR = "DELAY";
    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private final Properties properties;
    private List<InputStream> inputStreams;
    private final Long delay;
    private long sequenceNumber = 0L;

    public MyKafkaProducer() {
        this.delay = Long.valueOf(System.getenv(DELAY_ENV_VAR));
        this.properties = new Properties();
        this.properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        this.properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        loadFilesFromDir();
    }

    public void produce() {
        try {
            produceMessages();
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while producing messages: %s", e.getMessage()));
        }
    }

    private void produceMessages() throws IOException, InterruptedException {
        while (true) {
            for (InputStream inputStream : inputStreams) {
                Thread.sleep(delay);
                LOGGER.info("Waited " + delay + " MS =======================================================");
                sendDataToTopic(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    private void sendDataToTopic(String data) {
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_TO_SEND,
                    String.valueOf(++sequenceNumber),
                    data);
            kafkaProducer.send(record);
            kafkaProducer.flush();
            LOGGER.info(String.format("File read successfully with sequenceNumber: %d", sequenceNumber));
        }
    }

    private void loadFilesFromDir() {
        try (Stream<Path> path = Files.walk(Paths.get("private_data/"))) {
            inputStreams = path
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(this::generateInputStream)
                    .filter(Objects::nonNull)
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
}
