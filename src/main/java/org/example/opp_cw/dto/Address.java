package org.example.opp_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String province;
    private String city;
    private String street;
    @Indexed(unique = true)
    private String address;
    private String postalCode;
}
