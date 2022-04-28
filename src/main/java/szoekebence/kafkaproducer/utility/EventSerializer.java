package szoekebence.kafkaproducer.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import szoekebence.kafkaproducer.entity.Event;

import java.nio.charset.StandardCharsets;

public class EventSerializer implements Serializer<Event> {

    private final ObjectMapper objectMapper;

    public EventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String str, Event data) {
        try {
            return objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Event data) {
        try {
            return objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }
}
