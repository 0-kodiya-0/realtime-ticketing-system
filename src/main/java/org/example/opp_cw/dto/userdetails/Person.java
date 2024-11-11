package org.example.opp_cw.dto.userdetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.opp_cw.annotation.IsRegexValid;
import org.example.opp_cw.annotation.ValueOfEnum;
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
    @IsRegexValid(regexp = "^[a-z]+$")
    private String name;
    @IsRegexValid(regexp = "^[a-z]+$")
    private String surname;
    @ValueOfEnum(enumClass = Gender.class)
    private String gender;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Min(15)
    @Max(100)
    private int age;
    @Indexed(unique = true, partialFilter = "{ 'nic': { '$exists': true } }")
    @IsRegexValid(regexp = "^[0-9]+$", isNullable = true)
    private String nic;
    @Valid
    private Address address;
    @ValueOfEnum(enumClass = Nationality.class)
    private String nationality;
    private boolean isSystemAuthorized = false;
    private boolean isVisible = false;
    private boolean isDeleted = false;

    public Person() {
    }

    public static int calculateAge(LocalDate birthdate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);
        return period.getYears();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.age = calculateAge(dateOfBirth);
    }

    public void setAge(int age) {
        if (age == 0) {
            this.age = age;
        }
    }
}
