package org.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.backend.dto.userdetails.Person;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
    private ObjectId _id;
    private boolean isVip = false;
}
