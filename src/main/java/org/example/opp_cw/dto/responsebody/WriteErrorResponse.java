package org.example.opp_cw.dto.responsebody;

import com.mongodb.WriteError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class WriteErrorResponse extends ApiErrorResponse {

    public WriteErrorResponse(WriteError error, HttpServletRequest request) {
        super(HttpStatus.BAD_REQUEST, extractDuplicateKey(error.getMessage()), request);
        if (getError() == null) {
            System.out.println(error);
            setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            setError("Internal Server Error");
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
