package Model;

import java.time.LocalDate;

public class PersonalInfo {

    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final String address;
    private final String phoneNumber;

    public PersonalInfo(String lastName, String firstName, LocalDate birthday, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
