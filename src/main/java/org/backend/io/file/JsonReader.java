package org.backend.io.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JsonReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Optional<FileChunk> parseFilename(Path path, Pattern pattern) {
        String filename = path.getFileName().toString();
        Matcher matcher = pattern.matcher(filename);

        if (matcher.matches()) {
            String id = matcher.group(1);
            int chunkNumber = Integer.parseInt(matcher.group(2));
            return Optional.of(new FileChunk(path, id, chunkNumber));
        }
        return Optional.empty();
    }

    public static  <T> List<T> readChunkedFiles(String directoryPath, String prefix, String id, Class<T> targetClass) throws IOException {
        Pattern pattern = Pattern.compile("^" + prefix + "-(.*?)-(\\d+)-log\\.json$");
        List<FileChunk> chunks = new ArrayList<>();

        // Find matching files for the specific ID
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        parseFilename(path, pattern)
                                .filter(chunk -> chunk.id.equals(id))
                                .ifPresent(chunks::add);
                    });
        }

        if (chunks.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort chunks by number to maintain order
        chunks.sort(Comparator.comparingInt(chunk -> chunk.chunkNumber));

        // Read and combine all chunks
        List<T> combinedData = new ArrayList<>();
        for (FileChunk chunk : chunks) {
            List<T> chunkData = objectMapper.readValue(
                    chunk.path.toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass)
            );
            combinedData.addAll(chunkData);
        }

        return combinedData;
    }

    public static <T> T jsonToMap(String filePath, Class<T> mappingClass) throws IOException {
        return objectMapper.readValue(new File(filePath), mappingClass);
    }
}