package szoekebence.kafkaproducer.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import szoekebence.kafkaproducer.entity.Event;

import java.io.IOException;

public class EventDeserializer implements Deserializer<Event> {

    private final ObjectMapper objectMapper;

    public EventDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Event deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : objectMapper.readValue(data, Event.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Event deserialize(String topic, Headers headers, byte[] data) {
        try {
            return hasNoData(data) ? null : objectMapper.readValue(data, Event.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private boolean hasNoData(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }
}
