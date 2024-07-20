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


    public EmployeeDataService() {
        this.csvHandler = new CsvHandler(EMPLOYEE_DATA_FILE);
    }

    private String[] retrieveFileHeaders() throws IOException, CsvException {
        return csvHandler.retrieveFieldNames();
    }

    private boolean recordExists(String[] record) throws IOException, CsvException {
        List<String[]> data = csvHandler.retrieveCsvData(true);

        for (String[] row : data) {
            if (Arrays.equals(row, record)) {
                return true;
            }
        }
        return false;
    }

    public void addEmployeeRecord(String[] record) throws IOException, CsvException {
        List<String[]> data = csvHandler.retrieveCsvData(true);

        if (recordExists(record)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "Employee Record Exists",
                    "An employee record with the same details already exists. Please verify the information and try again.");
        }


        // Add record to data list
        data.add(record);

        // write data to the csv file
        csvHandler.writeDataToCsv(data, retrieveFileHeaders());
        System.out.println("Record saved");

    }

    public List<Employee> retrieveEmployeeData() throws IOException, CsvException {
        List<String[]> data = csvHandler.retrieveCsvData(true);
        List<Employee> employeeData = new ArrayList<>();

        for (String[] row : data) {
            employeeData.add(new Employee(
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
        return employeeData;
    }

    public Employee retrieveEmployeeById(int employeeId) throws IOException, CsvException {
        for (Employee employee : retrieveEmployeeData()) {
            if (employee.getEmployeeID() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    public void updateEmployeeRecord(String[] record) throws IOException, CsvException {
        List<String[]> data = csvHandler.retrieveCsvData(true);

        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row[0].equals(record[0])) {
                data.set(i, record);
            }
        }

        csvHandler.writeDataToCsv(data, retrieveFileHeaders());
    }

    public void deleteEmployeeRecord(int employeeId) throws IOException, CsvException {
        List<String[]> employeesRecord = csvHandler.retrieveCsvData(true);
        List<String[]> updatedEmployeesRecord = new ArrayList<>();

        for (String[] row : employeesRecord) {
            if (employeeId != Integer.parseInt(row[0])) {
                updatedEmployeesRecord.add(row);
            }
        }

        csvHandler.writeDataToCsv(updatedEmployeesRecord, retrieveFileHeaders());
        System.out.println("Record deleted");
    }
}
