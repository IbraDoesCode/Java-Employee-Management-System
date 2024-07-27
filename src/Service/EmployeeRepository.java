package Service;

import Model.Employee;
import Util.CsvHandler;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRecordService {

    private final CsvHandler csvHandler;

    private final String EMPLOYEE_DATA_FILE = "Data/employeeData.csv";

    private final List<String[]> employeeData;

    private final String[] HEADERS;

    public EmployeeRecordService() {
        this.csvHandler = new CsvHandler(EMPLOYEE_DATA_FILE);
        employeeData = csvHandler.retrieveCsvData(true);
        HEADERS = csvHandler.retrieveFieldNames();

    }

    public boolean recordExists(String[] record) {
        for (String[] row : employeeData) {

            String address = row[4];
            String phoneNum = row[5];
            String sss = row [7];
            String philhealth = row[8];
            String tin = row[9];
            String pagibig = row[10];

            if (record[4].equals(address) || record[5].equals(phoneNum) || record[7].equals(sss) ||
                    record[8].equals(philhealth) || record[9].equals(tin) || record[10].equals(pagibig)) {
                return true;
            }

        }
        return false;
    }

    public void addEmployeeRecord(String[] record) {

        if (recordExists(record)) {
            System.out.println("Record Already Exits");
            return;
        }

        employeeData.add(record);

        csvHandler.writeDataToCsv(employeeData, HEADERS);
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

        for (int i = 0; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            if (row[0].equals(record[0])) {
                employeeData.set(i, record);
                break;
            }
        }
        csvHandler.writeDataToCsv(employeeData, HEADERS);
    }

    public void deleteEmployeeRecord(int employeeId) {
        List<String[]> updatedEmployeesRecord = new ArrayList<>();

        for (String[] row : employeeData) {
            if (employeeId != Integer.parseInt(row[0])) {
                updatedEmployeesRecord.add(row);
            }
        }

        employeeData.clear();
        employeeData.addAll(updatedEmployeesRecord);

        csvHandler.writeDataToCsv(updatedEmployeesRecord, HEADERS);
        System.out.println("Record deleted");
    }
}
