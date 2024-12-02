package org.backend.server.microservices.authorization.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.microservices.authorization.enums.Gender;

import java.time.LocalDate;
import java.time.Period;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String surname;

    @NotNull
    @Column(nullable = false)
    private Gender gender;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private int age;

    @NotNull
    @Column(nullable = false)
    private String nic;

    @Embedded
    @Valid
    private Address address;

    @NotNull
    @Valid
    @Embedded
    @Column(nullable = false)
    private Contact contact;

    @Column(name = "isSystemAuthorized", nullable = false)
    private boolean systemAuthorized = false;

    @Column(name = "isVisible", nullable = false)
    private boolean visible = false;

    @Column(name = "isDeleted", nullable = false)
    private boolean deleted = false;

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
