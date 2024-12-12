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

/**
 * Define the logic that helps to convert .json file content in java data objects using ObjectMapper.
 */
public class JsonReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parses the filename of a given path to extract the file chunk information.
     *
     * @param path Path for the file that is to be matched
     * @param pattern Pattern to match against the filename
     * @return Optional containing the FileChunk if the filename matches the pattern, or an empty Optional if the filename does not match the pattern
     */
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

    /**
     * Define the logic to only read the chucked files that is related to unique id.
     *
     * @param directoryPath Directory file path where the file chunks are saved.
     * @param prefix Define the values that comes before id and the chuck.
     * @param id Unique file id.
     * @param targetClass The class used for mapping of the data that read as a string.
     * @return Returns a list with all the read chunks mapped to the given target class type.
     * @param <T> Define the data type for returning and mapping at runtime.
     * @throws IOException
     */
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

    /**
     * Define the logic for reading a one specific file .json file
     *
     * @param filePath Define path of the file that need to read.
     * @param targetClass The class used for mapping of the data that read as a string.
     * @return Returns the file data which is mapped to the given target class type.
     * @param <T> Define the data type for returning and mapping at runtime.
     * @throws IOException
     */
    public static <T> T jsonToMap(String filePath, Class<T> targetClass) throws IOException {
        return objectMapper.readValue(new File(filePath), targetClass);
    }
}