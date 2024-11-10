package org.example.opp_cw.dto.requestbody;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.annotation.IsPasswordValid;
import org.example.opp_cw.annotation.IsUserNameValid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    @IsUserNameValid
    private String userName;
    @NotBlank
    @IsPasswordValid
    private String password;
    @NotBlank
    @Email
    private String email;
}
