package szoeke.bence.kafkaproducer.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import szoeke.bence.kafkaproducer.utility.EventSerializer;

import java.util.Properties;

public class MyKafkaProducerConfiguration {

    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private static final String LINGER_MS_ENV_VAR = "LINGER_MS";
    private static final String BATCH_SIZE_IN_KB_ENV_VAR = "BATCH_SIZE_IN_KB";
    private static final String BUFFER_MEMORY_IN_KB_ENV_VAR = "BUFFER_MEMORY_IN_KB";

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, System.getenv(LINGER_MS_ENV_VAR));
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, getSizeInBytes(BATCH_SIZE_IN_KB_ENV_VAR));
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name);
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, getSizeInBytes(BUFFER_MEMORY_IN_KB_ENV_VAR));
        return properties;
    }

    private String getSizeInBytes(String envVarName) {
        return String.valueOf(Long.parseLong(System.getenv(envVarName)) * 1024L);
    }
}
