package org.example.opp_cw.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.enums.CountryCode;

@Data
public abstract class PhoneNumber {
    @NotNull
    @ValueOfEnum(enumClass = CountryCode.class)
    private String countryCode;
    @NotNull
    @Pattern(regexp = "^[0-9]+$", message = "invalid phone number")
    @Size(min = 10, max = 10)
    private String phoneNumber;
}
