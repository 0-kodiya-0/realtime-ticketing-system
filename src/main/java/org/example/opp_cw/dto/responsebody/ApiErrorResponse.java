package org.example.opp_cw.dto.responsebody;

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
public class ApiErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private Map<String, String> error;
    private String path;

    public ApiErrorResponse(HttpStatus status, String error, HttpServletRequest request) {
        this.status = status;
        this.error = new HashMap<>();
        this.error.put("message", error);
        this.path = request.getRequestURI();
    }

    public ApiErrorResponse(HttpStatus httpStatus, Map<String, String> error, HttpServletRequest request) {
        this.status = httpStatus;
        this.error = error;
        this.path = request.getRequestURI();
    }

    public void setError(String error) {
        this.error = new HashMap<>();
        this.error.put("message", error);
    }
}
