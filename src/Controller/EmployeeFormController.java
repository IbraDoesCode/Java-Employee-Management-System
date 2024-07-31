package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;


public class EmployeeFormController {

    // Constants
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
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
    private EmployeeDataService empDataService;
    private Employee employee;
    private boolean updateMode = false;
    private TextField[] textFields;

    @FXML
    private void initialize() {

        initializeComboBox();

        textFields = new TextField[]{lastNameTextField, firstNameTextField, phoneNoTextField, addressTextField,
                employeeIDTextField, positionTextField, supervisorTextField, tinNoTextField, sssNoTextField,
                philhealthNoTextField, pagibigNoTextField, basicSalaryTextField, riceTextField, clothingTextField, phoneTextField};

    }

    private void initializeComboBox() {
        departmentComboBox.setItems(FXCollections.observableArrayList(DEPARTMENTS));
        statusComboBox.setItems(FXCollections.observableArrayList(STATUSES));
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        updateMode = true;
        populateFields();
    }

    public void setEmployeeDataService(EmployeeDataService empDataService) {
        this.empDataService = empDataService;
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

                if (updateMode) {
                    empDataService.updateEmployeeRecord(record);
                } else {
                    empDataService.addEmployeeRecord(record);
                }
            }
        } catch (NumberFormatException e) {
            AlertUtil.showInvalidNumberFormatAlert();
        }
    }

    public void displayEmployeeDialog(boolean updateMode, Employee employee, EmployeeDataService empDataService, boolean showOnly) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeForm.fxml"));
            DialogPane empDetailsDialogPane = loader.load();

            EmployeeFormController controller = loader.getController();
            controller.setEmployeeDataService(empDataService);
            controller.setEmployeeID();

            if (updateMode) {
                controller.setEmployee(employee);
            } else if (employee != null) {
                controller.setEmployee(employee);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(empDetailsDialogPane);

            if (showOnly) {
                empDetailsDialogPane.lookupButton(ButtonType.OK).setVisible(false);
                empDetailsDialogPane.lookupButton(ButtonType.CANCEL).setVisible(false);
                controller.disableInputFields();
            }

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK && !showOnly) {
                controller.handleSaveRecord();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private void setEmployeeID() {
        employeeIDTextField.setText(String.valueOf(empDataService.getNewEmployeeID()));
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
