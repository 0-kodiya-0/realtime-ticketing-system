package org.backend.dto.userdetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.annotation.IsRegexValid;
import org.backend.annotation.ValueOfEnum;
import org.backend.enums.CountryCode;

@Data
@NoArgsConstructor
public class PhoneNumber {
    @ValueOfEnum(enumClass = CountryCode.class)
    private String countryCode;
    @IsRegexValid(regexp = "^[0-9]{10}+$")
    private String phoneNumber;
}
