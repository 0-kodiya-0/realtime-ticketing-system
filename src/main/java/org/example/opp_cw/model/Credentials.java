package org.example.opp_cw.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.example.opp_cw.annotation.IsObjectIdValid;
import org.example.opp_cw.annotation.IsPasswordValid;
import org.example.opp_cw.annotation.IsUserNameValid;
import org.example.opp_cw.dto.RandomSecureCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Document
@Data
public class Credentials {
    private ObjectId _id;
    @IsUserNameValid
    @Indexed(unique = true)
    private String userName;
    @NotNull
    private String password;

    private List<RandomSecureCode> backUpCodes;

    public void setPassword(@IsPasswordValid String password) {
        this.password = password;
    }

    public void setBackUpCodes(List<RandomSecureCode> backUpCodes) {
    }

    public void encodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        this.password = encoder.encode(this.password);
    }

    public void set_id(@IsObjectIdValid ObjectId _id) {
        this._id = _id;
    }
}
