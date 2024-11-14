package org.example.opp_cw.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.userdetails.Person;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
    private ObjectId _id;
    private boolean isVip = false;
}
