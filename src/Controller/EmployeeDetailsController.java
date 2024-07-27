package Controller;

import Model.Employee;
import Service.EmployeeRepository;
import Service.ObservableListService;
import Util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeFormController {

    private final EmployeeRepository empDataService;
    private ObservableListService observableListService;
    private Employee employee;
    private boolean updateMode = false;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final String[] DEPARTMENTS = {"Human Resources", "Payroll", "Information Technology", "Accounting", "Corporate", "Customer Service", "Logistics", "Sales"};
    private static final String[] STATUSES = {"Regular", "Part-time", "Probationary", "Intern", "Resigned"};

    @FXML
    private DialogPane dialogPane;

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
        empDataService = new EmployeeRepository();
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

    public boolean validateFields() {
        return areTextFieldsComplete() && isDatesInputValid();
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

    @FXML
    public void handleSaveRecord() {

        if (!(validateFields())) {
            AlertUtil.showIncompleteDataAlert();
            return;
        }

        try {
            if (AlertUtil.confirmDetails()) {
                String[] record = retrieveInputAsStringArray();

                if (updateMode) {
                    handleUpdateMode(record);
                } else {
                    if (handleAddMode(record)) {
                        return;
                    }
                }
                AlertUtil.showRecordSavedAlert();
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Number Format", "Please enter a valid number.");
        }
    }

    private boolean handleAddMode(String[] record) {
        if (empDataService.recordExists(record)) {
            AlertUtil.showDuplicateRecordExists();
            return true;
        }
        empDataService.addEmployeeRecord(record);
        observableListService.addEmployee(empDataService.convertArrayToEmployee(record));
        return false;
    }

    private void handleUpdateMode(String[] record) {
        empDataService.updateEmployeeRecord(record);
        Employee updatedEmployee = empDataService.convertArrayToEmployee(record);
        observableListService.updateEmployee(updatedEmployee);
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/Pages/EmployeeForm.fxml"));
            DialogPane employeeDialog = loader.load();


            EmployeeFormController employeeFormController = loader.getController();
            employeeFormController.setEmployeeTableService(empTableService);

            if (isUpdateMode && employee != null) {
                employeeFormController.setEmployee(employee);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(employeeDialog);
            dialog.setTitle(isUpdateMode ? "Edit Employee Details" : "Add New Employee");

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    employeeFormController.handleSaveRecord();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
