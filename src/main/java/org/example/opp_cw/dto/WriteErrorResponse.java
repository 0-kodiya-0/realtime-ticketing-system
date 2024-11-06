package org.example.opp_cw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.WriteError;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class WriteErrorResponse {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public WriteErrorResponse(WriteError error) {
        this.timestamp = LocalDateTime.now();
        this.status = HttpStatus.BAD_REQUEST;
        this.message = extractDuplicateKey(error.getMessage());
        if (message == null) {
            System.out.println(error);
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            this.message = "Internal Server Error";
        }
    }

    public static String extractDuplicateKey(String errorMessage) {
        String pattern = "dup key: \\{.*}";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null; // Return null if "dup key" part is not found
        }
    }
}
