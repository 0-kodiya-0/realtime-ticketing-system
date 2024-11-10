package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.opp_cw.annotation.IsPasswordValid;
import org.example.opp_cw.annotation.IsUserNameValid;
import org.example.opp_cw.dto.userdetails.Person;
import org.example.opp_cw.dto.userdetails.PhoneNumber;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignUpRequest extends Person {
    @NotBlank
    @IsUserNameValid
    private String userName;
    @NotBlank
    @IsPasswordValid
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Valid
    private PhoneNumber phoneNumber;
}
