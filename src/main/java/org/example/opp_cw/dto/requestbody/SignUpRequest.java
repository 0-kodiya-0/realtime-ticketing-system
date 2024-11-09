package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.opp_cw.dto.userdetails.Password;
import org.example.opp_cw.dto.userdetails.Person;
import org.example.opp_cw.dto.userdetails.PhoneNumber;
import org.example.opp_cw.dto.userdetails.UserName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignUpRequest extends Person {
    @Valid
    private UserName userName;
    @Valid
    private Password password;
    @Email
    private String email;
    @Valid
    private PhoneNumber phoneNumber;
}
