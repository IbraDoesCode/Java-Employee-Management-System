package service;

import model.*;
import util.AlertUtil;
import util.CsvHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDataService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private final CsvHandler csvHandler;
    private final List<String[]> employeeData;
    private final ObservableList<Employee> employeeRecords;

    public EmployeeDataService() {
        this.csvHandler = new CsvHandler("src/database/employeeData.csv");
        employeeData = csvHandler.retrieveCsvData();
        employeeRecords = FXCollections.observableArrayList(getAllEmployeeRecords());
    }

    public List<Employee> getAllEmployeeRecords() {
        List<Employee> employeesRecords = new ArrayList<>();

        for (int i = 1; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            employeesRecords.add(createEmployeeRecord(row));
        }
        return employeesRecords;
    }

    public ObservableList<Employee> getEmployeeList() {
        return employeeRecords;
    }

    public int getNewEmployeeID() {
        return Integer.parseInt(employeeData.getLast()[0]) + 1;
    }

    public boolean recordExists(String[] record, int currentEmpId) {
        for (int i = 1; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
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
        employeeRecords.add(createEmployeeRecord(record));

        csvHandler.appendDataToCsv(record);
        AlertUtil.showRecordSavedAlert();
        System.out.println("Record saved");
    }

    public void updateEmployeeRecord(String[] record) {
        if (recordExists(record, Integer.parseInt(record[0]))) {
            AlertUtil.showDuplicateRecordExists();
            return;
        }

        for (int i = 1; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            if (row[0].equals(record[0])) {
                employeeData.set(i, record);
                employeeRecords.set(i - 1, createEmployeeRecord(record));
                break;
            }
        }
        csvHandler.writeDataToCsv(employeeData);
        AlertUtil.showRecordUpdatedAlert();
        System.out.println("Record Updated");
    }

    public void deleteEmployeeRecord(int employeeId) {
        employeeData.removeIf(record -> record[0].equals(String.valueOf(employeeId)));
        employeeRecords.removeIf(employee -> employee.employeeId() == employeeId);

        csvHandler.writeDataToCsv(employeeData);
        AlertUtil.showRecordDeletedAlert();
        System.out.println("Record deleted");
    }

    private Employee createEmployeeRecord(String[] employeeData) {
        return new Employee(
                Integer.parseInt(employeeData[0]),
                employeeData[1],
                employeeData[2],
                LocalDate.parse(employeeData[3], DATE_FORMAT),
                employeeData[4],
                employeeData[5],
                LocalDate.parse(employeeData[6], DATE_FORMAT),
                employeeData[12],
                employeeData[13],
                employeeData[14],
                employeeData[11],
                employeeData[7],
                employeeData[8],
                employeeData[9],
                employeeData[10],
                Double.parseDouble(employeeData[15]),
                Double.parseDouble(employeeData[16]),
                Double.parseDouble(employeeData[17]),
                Double.parseDouble(employeeData[18]),
                Double.parseDouble(employeeData[19])
        );
    }
}
