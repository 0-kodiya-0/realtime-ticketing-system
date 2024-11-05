package org.example.opp_cw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.Address;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.Gender;
import org.example.opp_cw.enums.Privileges;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document("Admin")
@Data
@AllArgsConstructor
@CompoundIndex(def = "{'name': 1, 'surname': 1}", unique = true)
public class Admin {
    @MongoId
    private ObjectId id;
    private String name;
    private String surname;
    private int age;
    private Gender gender;
    private Date dateOfBirth;
    @Indexed(unique = true)
    private String nic;
    private Address address;
    private String nationality;
    private AccessLevel accessLevel;
    private List<Privileges> privileges;
    private boolean isAuthorized = false;
    private boolean isVisible = false;
    private boolean isDeleted = false;
}
