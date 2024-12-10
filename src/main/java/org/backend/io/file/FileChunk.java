package org.backend.io.file;

import java.nio.file.Path;

public class FileChunk {
    final Path path;
    final String id;
    final int chunkNumber;

    FileChunk(Path path, String id, int chunkNumber) {
        this.path = path;
        this.id = id;
        this.chunkNumber = chunkNumber;
    }
}