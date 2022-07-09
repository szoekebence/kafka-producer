package szoeke.bence.kafkaproducer.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkaproducer.utility.JsonNodeSerializer;

import java.util.Properties;

public class MyKafkaProducerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducerConfiguration.class);
    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private static final String LINGER_MS_ENV_VAR = "LINGER_MS";
    private static final String BATCH_SIZE_KB_ENV_VAR = "BATCH_SIZE_KB";
    private static final String LINGER_MS = System.getenv(LINGER_MS_ENV_VAR);
    private static final String BATCH_SIZE = generateSizeInBytes();

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonNodeSerializer.class.getName());
        properties.setProperty(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, "900000");
        setPropertiesForBatchProducing(properties);
        return properties;
    }

    private void setPropertiesForBatchProducing(Properties properties) {
        if (StringUtils.isNoneBlank(LINGER_MS, BATCH_SIZE)) {
            properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS);
            properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE);
            properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name);
            LOGGER.info("Properties set for batch producing.");
        }
    }

    private static String generateSizeInBytes() {
        return String.valueOf(Long.parseLong(System.getenv(MyKafkaProducerConfiguration.BATCH_SIZE_KB_ENV_VAR)) * 1024L);
    }
}
