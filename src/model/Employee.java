package model;

public class Employee {

    private final int employeeId;
    private final PersonalInfo personalInfo;
    private final EmploymentInfo employmentInfo;
    private final PayrollInfo payrollInfo;
    private final GovernmentIds governmentIds;

    public Employee(int employeeId, PersonalInfo personalInfo, EmploymentInfo employmentInfo, GovernmentIds governmentIds, PayrollInfo payrollInfo) {
        this.employeeId = employeeId;
        this.personalInfo = personalInfo;
        this.employmentInfo = employmentInfo;
        this.governmentIds = governmentIds;
        this.payrollInfo = payrollInfo;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }

    public GovernmentIds getGovernmentIds() {
        return governmentIds;
    }

    public PayrollInfo getPayrollInfo() {
        return payrollInfo;
    }

}
