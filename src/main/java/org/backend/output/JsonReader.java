package org.backend.output;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToMap(String filePath, Class<T> mappingClass) throws IOException {
        return mapper.readValue(new File(filePath), mappingClass);
    }
}
