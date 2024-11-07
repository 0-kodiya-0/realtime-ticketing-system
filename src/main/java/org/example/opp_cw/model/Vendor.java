package org.example.opp_cw.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.dto.Person;
import org.example.opp_cw.enums.VendorType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Vendor extends Person {
    @MongoId
    private String Id;
    @Pattern(regexp = "^[a-z]+$", message = "invalid vendor name")
    private String vendorName;
    @ValueOfEnum(enumClass = VendorType.class)
    private String vendorType;
    private boolean isVerified = false;
}
