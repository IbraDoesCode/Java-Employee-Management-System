package Service;

import Model.Employee;
import javafx.collections.ObservableList;

public class EmployeeTableService {

    private final ObservableList<Employee> employeeList;

    public EmployeeTableService(ObservableList<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public void addEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public void updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employeeList.size(); i++) {
            Employee emp = employeeList.get(i);
            if (emp.getEmployeeID() == (updatedEmployee.getEmployeeID())) {
                employeeList.set(i, updatedEmployee);
                return;
            }
        }
    }

    public void removeEmployee(Employee employee) {
        employeeList.remove(employee);
    }
}
