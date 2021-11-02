package datalayer.employee;

import java.util.Date;

public class EmployeeCreation {

    private String firstname, lastname, birthdate;

    public EmployeeCreation(String firstName, String lastName, String birthdate) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.birthdate = birthdate;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
