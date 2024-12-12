package org.backend.io.file;

import java.nio.file.Path;

/**
 * Represents a chunk of a large file during file writing or continuous file operations.
 *
 * This class is used to store information about a specific chunk of a file, including
 * the file path, a unique identifier, and the chunk number. It helps in managing and
 * organizing file chunks when dealing with large files or performing continuous file (ticket buying and selling)
 * operations to free up memory.
 */
public class FileChunk {
    final Path path;
    /**
     * Unique value to identify chunks that are related to each other.
     */
    final String id;
    /**
     * Value that represents the chunk count and chuck order for a specific file.
     */
    final int chunkNumber;


    /**
     * @param path File path of the chunk
     * @param id Unique identifier for the chunk
     * @param chunkNumber The sequence number of the chunk
     */
    FileChunk(Path path, String id, int chunkNumber) {
        this.path = path;
        this.id = id;
        this.chunkNumber = chunkNumber;
    }
}