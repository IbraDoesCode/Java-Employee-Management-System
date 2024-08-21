package model;


public class User {

    private final int employeeID;
    private final String username;
    private final String password;
    private final String lastName;
    private final String firstName;

    public User(int employeeID, String username, String password, String lastName, String firstName) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
