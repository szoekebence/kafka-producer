package szoeke.bence.kafkaproducer.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

import static java.util.Objects.isNull;

public class JsonNodeDeserializer implements Deserializer<JsonNode> {

    private final ObjectMapper objectMapper;

    public JsonNodeDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : objectMapper.readTree(data);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public JsonNode deserialize(String topic, Headers headers, byte[] data) {
        try {
            return hasNoData(data) ? null : objectMapper.readTree(data);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private boolean hasNoData(byte[] bytes) {
        return isNull(bytes) || bytes.length == 0;
    }
}
