package org.backend.dto;

public interface DataToDto<T extends Dto> {
    T toDto();
}
