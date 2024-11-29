package org.backend.dto.userdetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.annotation.IsRegexValid;

@Data
@NoArgsConstructor
public class Address {
    @IsRegexValid(regexp = "^[a-z]+$", isNullable = true)
    private String province;
    @IsRegexValid(regexp = "^[a-z]+$", isNullable = true)
    private String city;
    @IsRegexValid(regexp = "^[a-z]+$", isNullable = true)
    private String street;
    @IsRegexValid(regexp = "^[a-z]+$")
    private String address;
    @IsRegexValid(regexp = "^[0-9]+$")
    private String postalCode;
}
