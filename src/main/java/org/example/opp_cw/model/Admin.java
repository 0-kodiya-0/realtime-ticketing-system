package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.Privileges;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document("Admin")
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends Person {
    @MongoId
    private ObjectId id;
    private AccessLevel accessLevel;
    private List<Privileges> privileges;
}
