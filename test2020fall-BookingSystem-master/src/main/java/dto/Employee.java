package dto;

import java.util.Date;

public class Employee {

    private int id;
    private String firstname, lastname, birthdate;

    public Employee(String firstname, String lastname, String birthdate) {
    }

    public Employee(int id, String firstname, String lastname, String birthdate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
