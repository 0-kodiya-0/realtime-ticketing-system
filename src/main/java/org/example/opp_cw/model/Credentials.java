package org.example.opp_cw.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.example.opp_cw.annotation.IsObjectIdValid;
import org.example.opp_cw.annotation.IsPasswordValid;
import org.example.opp_cw.annotation.IsUserNameValid;
import org.example.opp_cw.dto.RandomSecureCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Document
@Data
public class Credentials {
    private ObjectId _id;
    @IsUserNameValid
    //    @Indexed(unique = true)
    private String userName;
    @NotNull
    private String password;

    private List<RandomSecureCode> backUpCodes;

    private List<GrantedAuthority> authority;

    public void setPassword(@IsPasswordValid String password) {
        this.password = password;
    }

    public void setBackUpCodes(List<RandomSecureCode> backUpCodes) {
    }

    public void set_id(@IsObjectIdValid ObjectId _id) {
        this._id = _id;
    }
}
