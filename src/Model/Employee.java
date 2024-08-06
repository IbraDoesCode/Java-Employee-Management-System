package Model;

import java.time.LocalDate;

public class Employee {

    private final int employeeID;
    private final String lastName;
    private final String firstName;
    private final LocalDate birthday;
    private final String address;
    private final String phoneNumber;
    private final LocalDate hireDate;
    private final String sssNumber;
    private final String philhealthNumber;
    private final String tinNumber;
    private final String pagibigNumber;
    private final String status;
    private final String position;
    private final String department;
    private final String immediateSupervisor;
    private final double basicSalary;
    private final double riceSubsidy;
    private final double phoneAllowance;
    private final double clothingAllowance;
    private final double hourlyRate;

    public Employee(int employeeID, String lastName, String firstName, LocalDate birthday, String address, String phoneNumber,
                    LocalDate hireDate, String sssNumber, String philhealthNumber, String tinNumber, String pagibigNumber,
                    String status, String position, String department, String immediateSupervisor,
                    double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double hourlyRate) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.status = status;
        this.position = position;
        this.department = department;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getPagibigNumber() {
        return pagibigNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

}
