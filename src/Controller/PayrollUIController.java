package Controller;

import Model.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PayrollUIController {

    @FXML
    private ComboBox<String> payTypeComboBox;

    @FXML
    private TextField basicSalaryTextField;

    @FXML
    private TextField semiMonthlyTextField;

    @FXML
    private TextField hourlyRateTextField;

    @FXML
    private TextField riceSubsidyTextField;

    @FXML
    private TextField clothingTextField;

    @FXML
    private TextField phoneTextField;

    private Employee employee;

    public void initializePayrollUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/PayrollUI.fxml"));
        Parent root = loader.load();
        PayrollUIController controller = loader.getController();
        controller.setEmployee(this.employee);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Calculate Payroll");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @FXML
    public void initialize() {
        initializeComboBox();
        populatePayrollDetails();
    }

    private void initializeComboBox() {
        String[] payType = {"Monthly", "Semi-Monthly", "Weekly"};
        payTypeComboBox.setItems(FXCollections.observableArrayList(payType));
    }

    private void populatePayrollDetails() {
        basicSalaryTextField.setText(String.valueOf(employee.getBasicSalary()));
        semiMonthlyTextField.setText(String.valueOf(employee.getGrossSemiMonthly()));
        hourlyRateTextField.setText(String.valueOf(employee.getHourlyRate()));
        riceSubsidyTextField.setText(String.valueOf(employee.getRiceSubsidy()));
        clothingTextField.setText(String.valueOf(employee.getClothingAllowance()));
        phoneTextField.setText(String.valueOf(employee.getPhoneAllowance()));
    }
}