package Service;

import Model.Employee;
import Util.AlertUtil;
import Util.CsvHandler;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDataService {

    private final CsvHandler csvHandler;

    private final String EMPLOYEE_DATA_FILE = "Data/employeeData.csv";

    private final List<String[]> employeeData;

    private final String[] HEADERS;

    public EmployeeDataService() {
        this.csvHandler = new CsvHandler(EMPLOYEE_DATA_FILE);
        employeeData = csvHandler.retrieveCsvData(true);
        HEADERS = csvHandler.retrieveFieldNames();

    }

    public boolean recordExists(String[] record, int currentEmpId) {
        for (String[] row : employeeData) {
            if (Integer.parseInt(row[0]) != currentEmpId && // EmployeeID
                (record[4].equals(row[4]) || // address
                record[5].equals(row[5]) || // phone number
                record[7].equals(row[7]) || // SSS
                record[8].equals(row[8]) || // PhilHealth
                record[9].equals(row[9]) || // TIN
                record[10].equals(row[10]))) { // Pag-IBIG
                return true;
            }
        }
        return false;
    }

    public void addEmployeeRecord(String[] record) {

        if (recordExists(record, -1)) {
            AlertUtil.showDuplicateRecordExists();
            return;
        }

        employeeData.add(record);

        csvHandler.writeDataToCsv(employeeData, HEADERS);
        AlertUtil.showRecordSavedAlert();
        System.out.println("Record saved");
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        for (String[] row : employeeData) {
            employees.add(convertArrayToEmployee(row));
        }
        return employees;
    }

    public Employee convertArrayToEmployee(String[] employeeData) {
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
        return Integer.parseInt(employeeData.getLast()[0]) + 1;
    }

    public void updateEmployeeRecord(String[] record) {

        if (recordExists(record, Integer.parseInt(record[0]))) {
            AlertUtil.showDuplicateRecordExists();
            return;
        }

        for (int i = 0; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            if (row[0].equals(record[0])) {
                employeeData.set(i, record);
                break;
            }
        }
        csvHandler.writeDataToCsv(employeeData, HEADERS);
        AlertUtil.showRecordUpdatedAlert();
        System.out.println("Record Updated");
    }

    public void deleteEmployeeRecord(int employeeId) {

        employeeData.removeIf(record -> record[0].equals(String.valueOf(employeeId)));

        csvHandler.writeDataToCsv(employeeData, HEADERS);
        AlertUtil.showRecordDeletedAlert();
        System.out.println("Record deleted");
    }
}
