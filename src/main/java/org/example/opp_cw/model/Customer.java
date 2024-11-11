package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.userdetails.Person;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
    private ObjectId _id;
    private boolean isVip = false;
}
