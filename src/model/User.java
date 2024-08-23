package model;


import service.EmployeeDataService;

public class User {

    private final int employeeID;
    private final String username;
    private final String password;
    private final String lastName;
    private final String firstName;
    private final EmployeeDataService empDataService;

    public User(int employeeID, String username, String password, String lastName, String firstName) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.empDataService = new EmployeeDataService();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public EmployeeDataService getEmpDataService() {
        return empDataService;
    }

    public void addEmployee(String[] record) {
        empDataService.addEmployeeRecord(record);
    }

    public void updateEmployee(String[] record) {
        empDataService.updateEmployeeRecord(record);
    }

    public void deleteEmployee(int employeeId) {
        empDataService.deleteEmployeeRecord(employeeId);
    }

}
