package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.userdetails.Person;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.Privileges;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends Person {
    @MongoId(FieldType.STRING)
    private String id;
    private AccessLevel accessLevel;
    private List<Privileges> privileges;
}
