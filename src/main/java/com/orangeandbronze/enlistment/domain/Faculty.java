package com.orangeandbronze.enlistment.domain;

import jakarta.persistence.*;
import java.util.*;
import static org.apache.commons.lang3.Validate.*;
@Entity
public class Faculty {
    @Id
    private final int facultyNumber;
    private final String firstname;
    private final String lastname;

    Faculty(int facultyNumber, String firstname, String lastname){
        isTrue (facultyNumber >= 0,
                "facultyNumber can't be negative, was: " + facultyNumber);
        notBlank(firstname);
        notBlank(lastname);
        this.facultyNumber = facultyNumber;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getFacultyNumber() {
        return facultyNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

}
