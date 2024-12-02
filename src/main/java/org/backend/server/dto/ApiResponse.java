package org.backend.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private Map<String, String> details;

    public ApiResponse(HttpStatus status, String details) {
        this.status = status;
        this.details = new HashMap<>();
        this.details.put("message", details);
    }

    public ApiResponse(HttpStatus httpStatus, Map<String, String> details) {
        this.status = httpStatus;
        this.details = details;
    }

    public ResponseEntity<ApiResponse> createResponse() {
        return new ResponseEntity<>(this, status);
    }

    public void setError(String details) {
        this.details = new HashMap<>();
        this.details.put("message", details);
    }
}
