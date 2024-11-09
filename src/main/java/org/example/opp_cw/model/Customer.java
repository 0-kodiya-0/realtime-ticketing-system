package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.userdetails.Person;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
    @MongoId(FieldType.STRING)
    private String id;
    private boolean isVip = false;
}
