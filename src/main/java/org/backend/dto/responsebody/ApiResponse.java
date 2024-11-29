package org.backend.dto.responsebody;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApiResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private Map<String, String> details;
    private String path;

    public ApiResponse(HttpStatus status, String details, HttpServletRequest request) {
        this.status = status;
        this.details = new HashMap<>();
        this.details.put("message", details);
        this.path = request.getRequestURI();
    }

    public ApiResponse(HttpStatus httpStatus, Map<String, String> details, HttpServletRequest request) {
        this.status = httpStatus;
        this.details = details;
        this.path = request.getRequestURI();
    }

    public void setError(String details) {
        this.details = new HashMap<>();
        this.details.put("message", details);
    }
}
