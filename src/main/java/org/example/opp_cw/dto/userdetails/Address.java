package org.example.opp_cw.dto.userdetails;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public abstract class Address {
    @Pattern(regexp = "^[a-z]+$", message = "invalid province")
    private String province;
    @Pattern(regexp = "^[a-z]+$", message = "invalid city")
    private String city;
    @Pattern(regexp = "^[a-z]+$", message = "invalid street")
    private String street;
    @Pattern(regexp = "^[a-z]+$", message = "invalid address")
    private String address;
    @Pattern(regexp = "^[0-9]+$", message = "invalid postalCode")
    private String postalCode;
}
