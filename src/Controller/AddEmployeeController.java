package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AddEmployeeController {

    private final EmployeeDataService empDataService;

    private ObservableList<Employee> employeeList;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private static final String[] DEPARTMENTS = {"Human Resources", "Payroll", "Information Technology","Accounting", "Corporate", "Customer Service", "Logistics", "Sales"};

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


    public AddEmployeeController() {
        empDataService = new EmployeeDataService();
    }

    public void setEmployeeList(ObservableList<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @FXML
    private void initialize() {
        initializeComboBox();
        employeeIDTextField.setText(String.valueOf(empDataService.getNewEmployeeID()));
    }

    private void initializeComboBox() {

        departmentComboBox.setItems(FXCollections.observableArrayList(DEPARTMENTS));

        statusComboBox.setItems(FXCollections.observableArrayList(STATUSES));
    }

    public void showAddNewEmployeeStage() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/AddEmployeeForm.fxml"));
            Parent root = loader.load();

            AddEmployeeController controller = loader.getController();
            controller.setEmployeeList(employeeList);

            Stage stage = new Stage();
            stage.setTitle("Add New Employee");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDatesInputValid() {
        return (dobDatePicker.getValue() != null && hireDatePicker.getValue() != null);
    }

    private boolean isTextFieldsComplete() {
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
                formatDateToString(hireDatePicker.getValue()),
                sssNoTextField.getText(), philhealthNoTextField.getText(), tinNoTextField.getText(), pagibigNoTextField.getText(),
                statusComboBox.getValue(), positionTextField.getText(), departmentComboBox.getValue(), supervisorTextField.getText(),
                basicSalaryTextField.getText(), riceTextField.getText(), phoneTextField.getText(), clothingTextField.getText(),
                String.format("%.2f", semiMonthlyRate), String.format("%.2f", hourlyRate)
        };

    }

    private void closeStage() {
        Stage addEmployeeStage = (Stage) firstNameTextField.getScene().getWindow();
        addEmployeeStage.close();
    }

    @FXML
    public void onSaveNewEmployeeRecord() {

        if (!isTextFieldsComplete() || !isDatesInputValid()) {
            AlertUtil.showAlert(Alert.AlertType.WARNING,
                    "Incomplete Data",
                    "Please ensure all required fields are filled out. ");
            return;
        }

        try {

            boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Details", "Please confirm that all the details are correct before saving.");

            if (confirmed) {

                String[] newEmployeeRecord = retrieveInputAsStringArray();
                empDataService.addEmployeeRecord(newEmployeeRecord);
                employeeList.add(empDataService.createNewEmployeeFromArrayInput(newEmployeeRecord));

                AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                        "Record Added Successfully",
                        "The employee record has been added successfully.");

                closeStage();
            }

        } catch (NumberFormatException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "Invalid Number Format",
                    "Please enter a valid number.");
        } catch (IOException | CsvException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "File Processing Error",
                    "An error occurred while processing the file. Please check the file and try again.");
        }
    }
}