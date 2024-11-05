package org.example.opp_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
public class Address {
    private String province;
    private String city;
    private String street;
    @Indexed(unique = true)
    private String address;
    private String postalCode;
}
