package org.example.opp_cw.dto.requestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.dto.RandomSecureCode;
import org.example.opp_cw.enums.ContactMethods;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactVerifyRequest {
    @ValueOfEnum(enumClass = ContactMethods.class)
    private String contactMethod;
    private RandomSecureCode code;

    public void setCode(String code) {
        this.code = new RandomSecureCode(code);
    }
}
