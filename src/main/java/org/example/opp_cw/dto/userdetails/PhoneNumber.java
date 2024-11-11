package org.example.opp_cw.dto.userdetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.annotation.IsRegexValid;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.enums.CountryCode;

@Data
@NoArgsConstructor
public class PhoneNumber {
    @ValueOfEnum(enumClass = CountryCode.class)
    private String countryCode;
    @IsRegexValid(regexp = "^[0-9]{10}+$")
    private String phoneNumber;
}
