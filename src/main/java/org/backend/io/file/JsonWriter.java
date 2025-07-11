package org.backend.io.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Define the logic that helps to convert java data object into .json file data using ObjectMapper.
 */
public class JsonWriter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Chuck size that each chunk can have
    private static final int DEFAULT_CHUNK_SIZE = 1000;

    /**
     * Writes the given data as chunked .json files in the specified directory.
     *
     * @param directoryPath The path of the directory to write the files in
     * @param prefix Prefix to use for the file names
     * @param id Unique id to include in the file names
     * @param data List of data to write as JSON
     * @param <T> Define the data type for mapping at runtime.
     * @throws IOException
     */
    public static <T> void writeChunkedJsonFiles(String directoryPath, String prefix, String id, List<T> data) throws IOException {
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

    /**
     * Write a list of data to a specific file without creating chucks
     *
     * @param dataList List of data that need to saved
     * @param filePath File path for the convert data that need to be saved
     */
    public static void writeToJsonPretty(List<Object> dataList, String filePath) {
        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write an object to a .json file
     *
     * @param data Data object
     * @param filePath File path for the convert data that need to be saved
     */
    public static void writeToJsonPretty(Object data, String filePath) {
        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}