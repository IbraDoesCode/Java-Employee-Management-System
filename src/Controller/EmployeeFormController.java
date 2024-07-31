package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Service.EmployeeListService;
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

    private final EmployeeDataService empDataService = new EmployeeDataService();
    private EmployeeListService employeeListService;
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

    private TextField[] textFields;

    @FXML
    private void initialize() {
        departmentComboBox.setItems(FXCollections.observableArrayList(DEPARTMENTS));
        statusComboBox.setItems(FXCollections.observableArrayList(STATUSES));

        textFields = new TextField[]{lastNameTextField, firstNameTextField, phoneNoTextField, addressTextField,
                employeeIDTextField, positionTextField, supervisorTextField, tinNoTextField, sssNoTextField,
                philhealthNoTextField, pagibigNoTextField, basicSalaryTextField, riceTextField, clothingTextField, phoneTextField};

        if (!updateMode) {
            employeeIDTextField.setText(String.valueOf(empDataService.getNewEmployeeID()));
        }
    }

    public void setEmpListService(EmployeeListService employeeListService) {
        this.employeeListService = employeeListService;
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
        if (!validateFields()) {
            AlertUtil.showIncompleteDataAlert();
            return;
        }

        try {
            if (AlertUtil.confirmDetails()) {
                String[] record = retrieveInputAsStringArray();

                if (updateMode) {
                    handleUpdateMode(record);
                } else {
                    handleAddMode(record);
                }
            }
        } catch (NumberFormatException e) {
            AlertUtil.showInvalidNumberFormatAlert();
        }
    }

    private void handleAddMode(String[] record) {
        empDataService.addEmployeeRecord(record);
        employeeListService.addEmployee(empDataService.convertArrayToEmployee(record));
    }

    private void handleUpdateMode(String[] record) {
        empDataService.updateEmployeeRecord(record);
        Employee updatedEmployee = empDataService.convertArrayToEmployee(record);
        employeeListService.updateEmployee(updatedEmployee);
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

    public void displayEmployeeDialog(boolean updateMode, Employee employee, EmployeeListService empListService, boolean showOnly) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeForm.fxml"));
            DialogPane empDetailsDialogPane = loader.load();

            EmployeeFormController controller = loader.getController();
            controller.setEmpListService(empListService);

            if (updateMode) {
                controller.setEmployee(employee);
            } else if (employee != null) {
                controller.setEmployee(employee);
                controller.disableAllFields();
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(empDetailsDialogPane);

            if (showOnly) {
                empDetailsDialogPane.lookupButton(ButtonType.OK).setVisible(false);
                empDetailsDialogPane.lookupButton(ButtonType.CANCEL).setVisible(false);
            }

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK && !showOnly) {
                controller.handleSaveRecord();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void disableAllFields() {
        for (TextField textField : textFields) {
            textField.setEditable(false);
        }
        dobDatePicker.setDisable(true);
        hireDatePicker.setDisable(true);
        departmentComboBox.setDisable(true);
        statusComboBox.setDisable(true);
    }
}
