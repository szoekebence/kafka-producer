package szoeke.bence.kafkaproducer.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileParser.class);

    private final JsonNodeDeserializer jsonNodeDeserializer;

    public FileParser(ObjectMapper objectMapper) {
        this.jsonNodeDeserializer = new JsonNodeDeserializer(objectMapper);
    }

    public List<JsonNode> generateJsonNodesFromFiles() {
        try (Stream<Path> path = Files.walk(Paths.get("private_data/"))) {
            return path
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(this::mapFileToInputStream)
                    .filter(Objects::nonNull)
                    .map(this::mapInputStreamToJsonNode)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error(String.format("File read failed: %s", e.getMessage()));
        }
        return null;
    }

    private InputStream mapFileToInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("File not found: %s", e.getMessage()));
        }
        return null;
    }

    private JsonNode mapInputStreamToJsonNode(InputStream inputStream) {
        try {
            return jsonNodeDeserializer.deserialize(null, inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Deserialization failed!", e);
        }
    }
}
