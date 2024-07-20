package Model;

public class User {

    private int employeeID;
    private String username;
    private String password;
    private String last_name;
    private String first_name;
    
    
    public User(int employeeID, String username, String password, String last_name, String first_name) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public boolean isValidCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
