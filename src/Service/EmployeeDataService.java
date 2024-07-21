package Service;

import Model.Employee;
import Util.AlertUtil;
import Util.CsvHandler;
import com.opencsv.exceptions.CsvException;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeDataService {

    private final CsvHandler csvHandler;
    private final String EMPLOYEE_DATA_FILE = "Data/employeeData.csv";
    private final List<String[]> employeeData;

    public EmployeeDataService() {
        this.csvHandler = new CsvHandler(EMPLOYEE_DATA_FILE);

        try {
            employeeData = csvHandler.retrieveCsvData(true);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing employee data", e);
        }

    }

    private String[] retrieveFileHeaders() throws IOException, CsvException {
        return csvHandler.retrieveFieldNames();
    }

    private boolean recordExists(String[] record) throws IOException, CsvException {
        for (String[] row : employeeData) {
            if (Arrays.equals(row, record)) {
                return true;
            }
        }
        return false;
    }

    public void addEmployeeRecord(String[] record) throws IOException, CsvException {
        if (recordExists(record)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "Employee Record Exists",
                    "An employee record with the same details already exists. Please verify the information and try again.");
            return;
        }

        employeeData.add(record);

        csvHandler.writeDataToCsv(employeeData, retrieveFileHeaders());
        System.out.println("Record saved");
    }

    public List<Employee> retrieveListOfEmployeeObject() throws IOException, CsvException {
        List<Employee> employees = new ArrayList<>();

        for (String[] row : employeeData) {
            employees.add(new Employee(
                    Integer.parseInt(row[0]),
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    row[5],
                    row[6],
                    row[7],
                    row[8],
                    row[9],
                    row[10],
                    row[11],
                    row[12],
                    row[13],
                    row[14],
                    Double.parseDouble(row[15]),
                    Double.parseDouble(row[16]),
                    Double.parseDouble(row[17]),
                    Double.parseDouble(row[18]),
                    Double.parseDouble(row[19]),
                    Double.parseDouble(row[20])
            ));
        }
        return employees;
    }

    public Employee createNewEmployeeFromArrayInput(String[] employeeData) {
        return new Employee(
                Integer.parseInt(employeeData[0]),
                employeeData[1],
                employeeData[2],
                employeeData[3],
                employeeData[4],
                employeeData[5],
                employeeData[6],
                employeeData[7],
                employeeData[8],
                employeeData[9],
                employeeData[10],
                employeeData[11],
                employeeData[12],
                employeeData[13],
                employeeData[14],
                Double.parseDouble(employeeData[15]),
                Double.parseDouble(employeeData[16]),
                Double.parseDouble(employeeData[17]),
                Double.parseDouble(employeeData[18]),
                Double.parseDouble(employeeData[19]),
                Double.parseDouble(employeeData[20])
        );
    }

    public int getNewEmployeeID() {
        try {
            int last_id = 34;
            for (Employee employee : retrieveListOfEmployeeObject()) {
                if (employee.getEmployeeID() > last_id) {
                    last_id = employee.getEmployeeID();
                }
            }
            return last_id + 1;

        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error retrieving new employee ID", e);
        }
    }

    public void updateEmployeeRecord(String[] record) throws IOException, CsvException {

        for (int i = 0; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            if (row[0].equals(record[0])) {
                employeeData.set(i, record);
            }
        }

        csvHandler.writeDataToCsv(employeeData, retrieveFileHeaders());
    }

    public void deleteEmployeeRecord(int employeeId) throws IOException, CsvException {
        List<String[]> updatedEmployeesRecord = new ArrayList<>();

        for (String[] row : employeeData) {
            if (employeeId != Integer.parseInt(row[0])) {
                updatedEmployeesRecord.add(row);
            }
        }

        employeeData.clear();
        employeeData.addAll(updatedEmployeesRecord);

        csvHandler.writeDataToCsv(updatedEmployeesRecord, retrieveFileHeaders());
        System.out.println("Record deleted");
    }
}
