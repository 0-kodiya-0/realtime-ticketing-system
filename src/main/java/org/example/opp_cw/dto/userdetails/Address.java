package org.example.opp_cw.dto.userdetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Pattern(regexp = "^[a-z]+$", message = "invalid province")
    private String province;
    @Pattern(regexp = "^[a-z]+$", message = "invalid city")
    private String city;
    @Pattern(regexp = "^[a-z]+$", message = "invalid street")
    private String street;
    @Pattern(regexp = "^[a-z]+$", message = "invalid address")
    @NotBlank
    private String address;
    @Pattern(regexp = "^[0-9]+$", message = "invalid postalCode")
    private String postalCode;
}
