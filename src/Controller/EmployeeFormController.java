package Controller;

import Model.Employee;
import Service.EmployeeRecordService;
import Service.ObservableListService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeFormController {

    private final EmployeeRecordService empDataService;
    private ObservableListService observableListService;
    private Employee employee;
    private boolean updateMode = false;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final String[] DEPARTMENTS = {"Human Resources", "Payroll", "Information Technology", "Accounting", "Corporate", "Customer Service", "Logistics", "Sales"};
    private static final String[] STATUSES = {"Regular", "Part-time", "Probationary", "Intern", "Resigned"};

    @FXML
    private TextField firstNameTextField, lastNameTextField, phoneNoTextField, addressTextField;
    @FXML
    private TextField employeeIDTextField, positionTextField, supervisorTextField;
    @FXML
    private TextField tinNoTextField, sssNoTextField, philhealthNoTextField, pagibigNoTextField;
    @FXML
    private TextField basicSalaryTextField, riceTextField, clothingTextField, phoneTextField;
    @FXML
    private DatePicker dobDatePicker, hireDatePicker;
    @FXML
    private ComboBox<String> departmentComboBox, statusComboBox;

    public EmployeeFormController() {
        empDataService = new EmployeeRecordService();
    }

    @FXML
    private void initialize() {

        departmentComboBox.setItems(FXCollections.observableArrayList(DEPARTMENTS));

        statusComboBox.setItems(FXCollections.observableArrayList(STATUSES));

        if (!updateMode) {
            employeeIDTextField.setText(String.valueOf(empDataService.getNewEmployeeID()));
        }
    }


    public void setEmployeeTableService(ObservableListService observableListService) {
        this.observableListService = observableListService;
    }


    public void setEmployee(Employee employee) {
        this.employee = employee;
        updateMode = true;
        populateFields();
    }

    private boolean isDatesInputValid() {
        return dobDatePicker.getValue() != null && hireDatePicker.getValue() != null;
    }

    private boolean areTextFieldsComplete() {
        TextField[] textFields = {
                lastNameTextField, firstNameTextField, phoneNoTextField, addressTextField,
                employeeIDTextField, positionTextField, supervisorTextField,
                tinNoTextField, sssNoTextField, philhealthNoTextField, pagibigNoTextField,
                basicSalaryTextField, riceTextField, clothingTextField, phoneTextField
        };

        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String formatDateToString(LocalDate localDate) {
        return localDate.format(DATE_FORMAT);
    }

    private String[] retrieveInputAsStringArray() {
        double basicSalary = Double.parseDouble(basicSalaryTextField.getText());
        double semiMonthlyRate = basicSalary / 2;
        double hourlyRate = (basicSalary / 21) / 8;

        return new String[]{
                employeeIDTextField.getText(), lastNameTextField.getText(), firstNameTextField.getText(),
                formatDateToString(dobDatePicker.getValue()), addressTextField.getText(), phoneNoTextField.getText(),
                formatDateToString(hireDatePicker.getValue()), sssNoTextField.getText(), philhealthNoTextField.getText(),
                tinNoTextField.getText(), pagibigNoTextField.getText(), statusComboBox.getValue(), positionTextField.getText(),
                departmentComboBox.getValue(), supervisorTextField.getText(), basicSalaryTextField.getText(),
                riceTextField.getText(), phoneTextField.getText(), clothingTextField.getText(),
                String.format("%.2f", semiMonthlyRate), String.format("%.2f", hourlyRate)
        };
    }

    private void closeStage() {
        Stage stage = (Stage) firstNameTextField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleSaveRecord() {
        if (!areTextFieldsComplete() || !isDatesInputValid()) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please ensure all required fields are filled out.");
            return;
        }

        try {
            boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Details", "Please confirm that all the details are correct before saving.");
            if (confirmed) {
                String[] record = retrieveInputAsStringArray();

                if (updateMode) {
                    empDataService.updateEmployeeRecord(record);
                    Employee updatedEmployee = empDataService.createNewEmployeeFromArrayInput(record);
                    observableListService.updateEmployee(updatedEmployee);
                } else {
                    empDataService.addEmployeeRecord(record);
                    observableListService.addEmployee(empDataService.createNewEmployeeFromArrayInput(record));
                }

                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Record Saved", "The employee record has been saved successfully.");
                closeStage();
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Number Format", "Please enter a valid number.");
        }
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateString);
            return null;
        }
    }

    private void populateFields() {
        employeeIDTextField.setText(String.valueOf(employee.getEmployeeID()));
        firstNameTextField.setText(employee.getFirstName());
        lastNameTextField.setText(employee.getLastName());
        phoneNoTextField.setText(employee.getPhoneNumber());
        addressTextField.setText(employee.getAddress());
        positionTextField.setText(employee.getPosition());
        supervisorTextField.setText(employee.getImmediateSupervisor());
        tinNoTextField.setText(employee.getTinNumber());
        sssNoTextField.setText(employee.getSssNumber());
        philhealthNoTextField.setText(employee.getPhilhealthNumber());
        pagibigNoTextField.setText(employee.getPagibigNumber());
        basicSalaryTextField.setText(String.valueOf(employee.getBasicSalary()));
        riceTextField.setText(String.valueOf(employee.getRiceSubsidy()));
        clothingTextField.setText(String.valueOf(employee.getClothingAllowance()));
        phoneTextField.setText(String.valueOf(employee.getPhoneAllowance()));
        dobDatePicker.setValue(convertStringToLocalDate(employee.getBirthday()));
        hireDatePicker.setValue(convertStringToLocalDate(employee.getHireDate()));
        departmentComboBox.setValue(employee.getDepartment());
        statusComboBox.setValue(employee.getStatus());
    }

    public void showEmployeeForm(boolean isUpdateMode, Employee employee, ObservableListService empTableService) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/EmployeeForm.fxml"));
            Parent root = loader.load();

            EmployeeFormController employeeFormController = loader.getController();
            employeeFormController.setEmployeeTableService(empTableService);

            if (isUpdateMode && employee != null) {
                employeeFormController.setEmployee(employee);
            }

            Stage stage = new Stage();
            stage.setTitle(isUpdateMode ? "Update Employee" : "Add New Employee");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Form Error", "An error occurred while loading the employee form.");
        }
    }
}
