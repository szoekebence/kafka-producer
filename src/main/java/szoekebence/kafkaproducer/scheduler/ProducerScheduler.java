package szoekebence.kafkaproducer.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ProducerScheduler implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC_NAME = "streams-input";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerScheduler.class);

    @Override
    public void run(String... args) {
        try {
            produceMessages();
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred during message producing: %s", e.getMessage()));
        } finally {
            System.exit(0);
        }
    }

    private void produceMessages() throws IOException {
        kafkaTemplate.send(TOPIC_NAME, readFile());
        LOGGER.info("File send successful.");
    }

    private String readFile() throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        Path path = Paths.get("src/main/resources/private_data/customer_lab_mtas_ebm_feb_2022/A20220209.2139+0000-20220209.2140+0000_1_ims.json");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        LOGGER.info("File read successful.");
        return resultStringBuilder.toString();
    }
}
