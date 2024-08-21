package model;

import java.time.LocalDate;

public class EmploymentInfo {

    private final LocalDate hireDate;
    private final String position;
    private final String department;
    private final String immediateSupervisor;
    private final String status;

    public EmploymentInfo(LocalDate hireDate, String position, String department, String immediateSupervisor, String status) {
        this.hireDate = hireDate;
        this.position = position;
        this.department = department;
        this.immediateSupervisor = immediateSupervisor;
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getDepartment() {
        return department;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public String getStatus() {
        return status;
    }
}
