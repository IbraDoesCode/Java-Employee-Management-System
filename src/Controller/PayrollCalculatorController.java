package Controller;

import Model.Employee;
import Service.PayrollService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PayrollCalculatorController {

    @FXML
    private TextField IdTextField, nameTextField, basicTextField;

    @FXML
    private TextField hourlyRateTextField, hoursWorkedTextField, OtTextField, OtPayTextField, regularPayTextField;

    @FXML
    private TextField riceSubsidyTextField, clothingTextField, phoneTextField;

    @FXML
    private TextField sssTextField, pagibigTextField, philhealthTextField;

    @FXML
    private TextField grossPayTextField, contributionsTextField, taxableIncomeTextField, taxTextField, netPayTextField;

    private Employee employee;

    @FXML
    public void initialize() {

        hoursWorkedTextField.textProperty().addListener(((e) -> populatePayDetails()));
        OtTextField.textProperty().addListener((e) -> populatePayDetails());
    }

    public void showPayrollForm(Employee employee) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/PayrollCalculator.fxml"));
        Parent root = loader.load();

        PayrollCalculatorController controller = loader.getController();
        controller.setEmployee(employee);

        Stage stage = new Stage();
        stage.setTitle("MotorPH Compute Payroll");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        populateFields();
    }


    private void populateFields() {

        IdTextField.setText(String.valueOf(employee.getEmployeeID()));
        nameTextField.setText(employee.getFirstName() + " " + employee.getLastName());
        basicTextField.setText(String.valueOf(employee.getBasicSalary()));
        hourlyRateTextField.setText(String.valueOf(employee.getHourlyRate()));
        riceSubsidyTextField.setText(String.valueOf(employee.getRiceSubsidy()));
        clothingTextField.setText(String.valueOf(employee.getClothingAllowance()));
        phoneTextField.setText(String.valueOf(employee.getPhoneAllowance()));

    }

    private void populatePayDetails() {

        if (employee == null) {
            return;
        }

        try {
            double hoursWorked = Double.parseDouble(hoursWorkedTextField.getText());
            double overtime = Double.parseDouble(OtTextField.getText());

            PayrollService payrollService = new PayrollService(employee, hoursWorked, overtime);
            double regularPay = payrollService.basicSalary();
            double otPay = payrollService.overTimePay();

            double sss = payrollService.calculateSssContribution();
            double hdmf = payrollService.calculatePagIbigContribution();
            double phic = payrollService.calculatePhilhealthContribution();
            double totalContributions = payrollService.calculatePartialDeductions();

            double gross = payrollService.calculateGrossPay();
            double taxableIncome = payrollService.taxableIncome();
            double tax = payrollService.calculateWithholdingTax();
            double net = payrollService.calculateNetPay();

            regularPayTextField.setText(String.format("%.2f", regularPay));
            OtPayTextField.setText(String.format("%.2f", otPay));

            sssTextField.setText(String.format("%.2f", sss));
            pagibigTextField.setText(String.format("%.2f", hdmf));
            philhealthTextField.setText(String.format("%.2f", phic));

            grossPayTextField.setText(String.format("%.2f", gross));
            contributionsTextField.setText(String.format("%.2f", totalContributions));
            taxableIncomeTextField.setText(String.format("%.2f", taxableIncome));
            taxTextField.setText(String.format("%.2f", tax));
            netPayTextField.setText(String.format("%.2f", net));

        } catch (NumberFormatException | ArithmeticException | NullPointerException e) {
            regularPayTextField.setText("0.00");
            OtPayTextField.setText("0.00");
            sssTextField.setText("0.00");
            pagibigTextField.setText("0.00");
            philhealthTextField.setText("0.00");
            grossPayTextField.setText("0.00");
            contributionsTextField.setText("0.00");
            taxTextField.setText("0.00");
            netPayTextField.setText("0.00");
        }
    }
}
