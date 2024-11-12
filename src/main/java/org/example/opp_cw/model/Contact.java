package org.example.opp_cw.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.annotation.IsObjectIdValid;
import org.example.opp_cw.annotation.IsRegexValid;
import org.example.opp_cw.dto.userdetails.PhoneNumber;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Contact {
    private Object _id;
    //    @Indexed(unique = true)
    @IsRegexValid(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")
    private String email;
    private boolean isEmailVerified = false;
    @IsRegexValid(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", isNullable = true)
    private String recoveryEmail;
    private boolean isRecoveryEmailVerified = false;
    //    @Indexed(unique = true)
    @NotNull
    @Valid
    private PhoneNumber phoneNumber;
    private boolean phoneNumberVerified = false;
    @Valid
    private PhoneNumber recoveryPhoneNumber;
    private boolean isRecoveryPhoneNumberVerified = false;

    public void set_id(@IsObjectIdValid Object _id) {
        this._id = _id;
    }
}
