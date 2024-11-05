package org.example.opp_cw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.dto.Address;
import org.example.opp_cw.enums.Gender;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@CompoundIndex(def = "{'name': 1, 'surname': 1}", unique = true)
public class Person {
    private String name;
    private String surname;
    private int age;
    private Gender gender;
    private Date dateOfBirth;
    @Indexed(unique = true)
    private String nic;
    private Address address;
    private String nationality;
    private boolean isAuthorized = false;
    private boolean isVisible = false;
    private boolean isDeleted = false;
}
