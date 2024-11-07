package org.example.opp_cw.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.opp_cw.dto.PhoneNumber;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Contact {
    @Email
    private String email;
    private boolean isEmailVerified = false;
    @Email
    private String recoveryEmail;
    private boolean isRecoveryEmailVerified = false;
    @Valid
    private PhoneNumber phoneNumber;
    private boolean phoneNumberVerified = false;
    @Valid
    private PhoneNumber recoveryPhoneNumber;
    private boolean isRecoveryPhoneNumberVerified = false;
}
