package Service;

import Model.Employee;
import javafx.collections.ObservableList;

public class EmployeeListService {

    private final ObservableList<Employee> employeeList;

    public EmployeeListService(ObservableList<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    private boolean recordExists(Employee newEmp) {
        for (Employee emp : employeeList) {

            if (emp.getEmployeeID() == newEmp.getEmployeeID() ||
                emp.getPhoneNumber().equals(newEmp.getPhoneNumber()) ||
                emp.getAddress().equals(newEmp.getAddress()) ||
                emp.getTinNumber().equals(newEmp.getTinNumber()) ||
                emp.getSssNumber().equals(newEmp.getSssNumber()) ||
                emp.getPhilhealthNumber().equals(newEmp.getPhilhealthNumber()) ||
                emp.getPagibigNumber().equals(newEmp.getPagibigNumber())) {

                return true;
            }
        }
        return false;
    }

    public void addEmployee(Employee employee) {
        if (recordExists(employee)) {
            return;
        }
        employeeList.add(employee);
    }

    public void updateEmployee(Employee updatedEmployee) {
        if (recordExists(updatedEmployee)){
            return;
        }

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
