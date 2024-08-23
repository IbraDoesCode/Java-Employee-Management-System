package controller;

import model.Employee;
import model.Mode;
import model.User;
import util.AlertUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EmployeeFormController {

    // Constants
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final String[] DEPARTMENTS = {"Human Resources", "Payroll", "Information Technology", "Accounting", "Corporate", "Customer Service", "Logistics", "Sales"};
    private static final String[] STATUSES = {"Regular", "Part-time", "Probationary", "Intern", "Resigned"};

    // UI Properties
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNoTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField supervisorTextField;
    @FXML
    private TextField tinNoTextField;
    @FXML
    private TextField sssNoTextField;
    @FXML
    private TextField philhealthNoTextField;
    @FXML
    private TextField pagibigNoTextField;
    @FXML
    private TextField basicSalaryTextField;
    @FXML
    private TextField riceTextField;
    @FXML
    private TextField clothingTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private DatePicker hireDatePicker;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private ComboBox<String> statusComboBox;

    // Other Properties
    private User user;
    private Employee employee;
    private Mode mode;
    private TextField[] textFields;

    @FXML
    private void initialize() {

        initializeComboBox();

        textFields = new TextField[]{lastNameTextField, firstNameTextField, phoneNoTextField, addressTextField,
                employeeIDTextField, positionTextField, supervisorTextField, tinNoTextField, sssNoTextField,
                philhealthNoTextField, pagibigNoTextField, basicSalaryTextField, riceTextField, clothingTextField, phoneTextField};

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        populateFields();
    }

    public void setMode(Mode mode) {
        this.mode = mode;

        if (mode == Mode.VIEW) {
            disableInputFields();
        } else if (mode == Mode.ADD) {
            populateNewEmpId();
        }
    }

    private void initializeComboBox() {
        departmentComboBox.setItems(FXCollections.observableArrayList(DEPARTMENTS));
        statusComboBox.setItems(FXCollections.observableArrayList(STATUSES));
    }

    @FXML
    public void handleSaveRecord() {
        if (!validateFields()) {
            AlertUtil.showIncompleteDataAlert();
            return;
        }

        try {
            if (AlertUtil.confirmDetails()) {
                String[] record = retrieveInputAsStringArray();

                if (mode == Mode.UPDATE) {
                    user.updateEmployee(record);
                } else if (mode == Mode.ADD) {
                    user.addEmployee(record);
                }
            }
        } catch (NumberFormatException e) {
            AlertUtil.showInvalidNumberFormatAlert();
        }
    }

    private boolean isDatesInputValid() {
        return dobDatePicker.getValue() != null && hireDatePicker.getValue() != null;
    }

    private boolean areTextFieldsComplete() {
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean validateFields() {
        return areTextFieldsComplete() && isDatesInputValid();
    }

    private String formatDateToString(LocalDate localDate) {
        return localDate.format(DATE_FORMAT);
    }

    private String[] retrieveInputAsStringArray() {
        double basicSalary = Double.parseDouble(basicSalaryTextField.getText());
        double hourlyRate = (basicSalary / 21) / 8;

        return new String[]{
                employeeIDTextField.getText(), lastNameTextField.getText(), firstNameTextField.getText(),
                formatDateToString(dobDatePicker.getValue()), addressTextField.getText(), phoneNoTextField.getText(),
                formatDateToString(hireDatePicker.getValue()), sssNoTextField.getText(), philhealthNoTextField.getText(),
                tinNoTextField.getText(), pagibigNoTextField.getText(), statusComboBox.getValue(), positionTextField.getText(),
                departmentComboBox.getValue(), supervisorTextField.getText(), basicSalaryTextField.getText(),
                riceTextField.getText(), phoneTextField.getText(), clothingTextField.getText(), String.format("%.2f", hourlyRate)
        };
    }

    private void populateFields() {
        employeeIDTextField.setText(String.valueOf(employee.getEmployeeId()));
        firstNameTextField.setText(employee.getPersonalInfo().getFirstName());
        lastNameTextField.setText(employee.getPersonalInfo().getLastName());
        phoneNoTextField.setText(employee.getPersonalInfo().getPhoneNumber());
        addressTextField.setText(employee.getPersonalInfo().getAddress());
        positionTextField.setText(employee.getEmploymentInfo().getPosition());
        supervisorTextField.setText(employee.getEmploymentInfo().getImmediateSupervisor());
        tinNoTextField.setText(employee.getGovernmentIds().getTinNumber());
        sssNoTextField.setText(employee.getGovernmentIds().getSssNumber());
        philhealthNoTextField.setText(employee.getGovernmentIds().getPhilhealthNumber());
        pagibigNoTextField.setText(employee.getGovernmentIds().getPagibigNumber());
        basicSalaryTextField.setText(String.valueOf(employee.getPayrollInfo().getBasicSalary()));
        riceTextField.setText(String.valueOf(employee.getPayrollInfo().getRiceSubsidy()));
        clothingTextField.setText(String.valueOf(employee.getPayrollInfo().getClothingAllowance()));
        phoneTextField.setText(String.valueOf(employee.getPayrollInfo().getPhoneAllowance()));
        dobDatePicker.setValue(employee.getPersonalInfo().getBirthday());
        hireDatePicker.setValue(employee.getEmploymentInfo().getHireDate());
        departmentComboBox.setValue(employee.getEmploymentInfo().getDepartment());
        statusComboBox.setValue(employee.getEmploymentInfo().getStatus());
    }

    private void populateNewEmpId() {
        employeeIDTextField.setText(String.valueOf(user.getEmpDataService().getNewEmployeeID()));
    }

    private void disableInputFields() {
        for (TextField textField : textFields) {
            textField.setEditable(false);
        }
        dobDatePicker.setDisable(true);
        hireDatePicker.setDisable(true);
        departmentComboBox.setDisable(true);
        statusComboBox.setDisable(true);
    }

}
