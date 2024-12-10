package org.backend.io.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int DEFAULT_CHUNK_SIZE = 1000;

    public static <T> void writeChunkedJsonFiles(String directoryPath, String prefix, String id,
                                                 List<T> data) throws IOException {
        if (data == null || data.isEmpty()) {
            return;
        }

        // Create chunks
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < data.size(); i += DEFAULT_CHUNK_SIZE) {
            int end = Math.min(data.size(), i + DEFAULT_CHUNK_SIZE);
            chunks.add(data.subList(i, end));
        }

        // Write each chunk to a file
        for (int i = 0; i < chunks.size(); i++) {
            String fileName = String.format("%s-%s-%d-log.json", prefix, id, i + 1);
            Path filePath = Paths.get(directoryPath, fileName);

            objectMapper.writeValue(filePath.toFile(), chunks.get(i));
        }
    }

    public static void writeToJsonPretty(List<Object> dataList, String filePath) {
        try {
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToJsonPretty(Object data, String filePath) {
        try {
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}