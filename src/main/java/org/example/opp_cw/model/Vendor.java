package org.example.opp_cw.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.example.opp_cw.annotation.IsObjectIdValid;
import org.example.opp_cw.annotation.IsRegexValid;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.enums.VendorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Vendor {
    private ObjectId _id;
    @IsRegexValid(regexp = "^[a-z]+$")
    private String vendorName;
    @ValueOfEnum(enumClass = VendorType.class)
    private String vendorType;
    private boolean isVerified = false;

    public void set_id(@IsObjectIdValid ObjectId _id) {
        this._id = _id;
    }
}
