package org.backend.dto.requestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.annotation.ValueOfEnum;
import org.backend.dto.RandomSecureCode;
import org.backend.enums.ContactMethods;

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
