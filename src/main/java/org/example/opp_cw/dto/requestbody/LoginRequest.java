package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.dto.userdetails.Password;
import org.example.opp_cw.dto.userdetails.UserName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Valid
    private UserName userName;
    @Valid
    private Password password;
    @Email
    private String email;
}
