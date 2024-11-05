package org.example.opp_cw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.Privileges;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.lang.NonNull;

import java.util.List;

@Document("Admin")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends Person {
    @MongoId
    private ObjectId id;
    private @NonNull AccessLevel accessLevel;
    private @NonNull List<Privileges> privileges;
}
