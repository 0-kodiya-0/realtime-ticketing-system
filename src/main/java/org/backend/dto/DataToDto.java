package org.backend.dto;

/**
 * An interface for classes responsible for converting objects into their corresponding DTO representations
 * @param <T>
 */
public interface DataToDto<T extends Dto> {
    /**
     * Placeholder for implementers to provide the logic for converting class data to a DTO.
     *
     * @return The DTO representation of the class data.
     */
    T toDto();
}
