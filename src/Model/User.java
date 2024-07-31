package Model;


public class User {

    private final int employeeID;
    private final String username;
    private final String password;
    private final String last_name;
    private final String first_name;
    
    
    public User(int employeeID, String username, String password, String last_name, String first_name) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValidCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
