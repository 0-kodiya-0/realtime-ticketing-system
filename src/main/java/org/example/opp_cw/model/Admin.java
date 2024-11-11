package org.example.opp_cw.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.userdetails.Person;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.Privileges;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends Person {
    private ObjectId _id;
    private AccessLevel accessLevel;
    private List<Privileges> privileges;
}
