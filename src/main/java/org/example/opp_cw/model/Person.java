package org.example.opp_cw.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;
import org.example.opp_cw.annotation.ValueOfEnum;
import org.example.opp_cw.dto.Address;
import org.example.opp_cw.enums.Gender;
import org.example.opp_cw.enums.Nationality;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

@Data
@CompoundIndex(def = "{'name': 1, 'surname': 1}", unique = true)
public abstract class Person {
    @NonNull
    @Pattern(regexp = "^[a-z]+$", message = "invalid name")
    private String name;
    @NonNull
    @Pattern(regexp = "^[a-z]+$", message = "invalid surname")
    private String surname;
    @NonNull
    @ValueOfEnum(enumClass = Gender.class)
    private String gender;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Min(15)
    @Max(100)
    private int age;
    @Indexed(unique = true, partialFilter = "{ 'nic': { '$exists': true } }")
    @Pattern(regexp = "^[0-9]+$", message = "invalid nic")
    private String nic;
    @Valid
    private Address address;
    @NonNull
    @ValueOfEnum(enumClass = Nationality.class)
    private String nationality;
    private boolean isAuthorized = false;
    private boolean isVisible = false;
    private boolean isDeleted = false;

    public Person() {
    }

    public static int calculateAge(LocalDate birthdate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);
        return period.getYears();
    }

    public void setDateOfBirth(@NonNull LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.age = calculateAge(dateOfBirth);
    }

    public void setAge(int age) {
        if (age == 0) {
            this.age = age;
        }
    }
}
