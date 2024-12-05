package org.backend.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date timestamp = new Date();
    private HttpStatus status;
    private Map<String, Object> details;

    public ApiResponse(HttpStatus status, String details) {
        this.status = status;
        this.details = new HashMap<>();
        this.details.put("message", details);
    }

    public ApiResponse(HttpStatus httpStatus, Map<String, Object> details) {
        this.status = httpStatus;
        this.details = details;
    }

    public ResponseEntity<ApiResponse> createResponse() {
        return new ResponseEntity<>(this, status);
    }

    public String createJsonResponse() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
