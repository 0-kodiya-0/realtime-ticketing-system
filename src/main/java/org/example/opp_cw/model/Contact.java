package org.example.opp_cw.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.example.opp_cw.annotation.IsObjectIdValid;
import org.example.opp_cw.dto.userdetails.PhoneNumber;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
public class Contact {
    @MongoId(FieldType.STRING)
    private String id;
    @IsObjectIdValid
    private String ownerId;
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
