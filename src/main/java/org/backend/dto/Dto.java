package org.backend.dto;

import lombok.Data;

/**
 * An abstract class that defines the structure for dto. Includes an id so that it enforces implementation of parsing logic in subclasses
 */
@Data
public abstract class Dto {
    private String id;
}
