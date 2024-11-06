package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("Customer")
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
    @MongoId
    private ObjectId id;
    private boolean isVip = false;
}
