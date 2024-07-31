package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Service.EmployeeListService;
import Util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeListController {

    private final EmployeeDataService empDataService;

    private final ObservableList<Employee> employeeObservableList;

    private final EmployeeListService employeeListService;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn, lastNameColumn, tinNoColumn, sssNoColumn, philhealthNoColumn, pagibigNoColumn;

    public EmployeeListController() {
        empDataService = new EmployeeDataService();
        employeeObservableList = FXCollections.observableArrayList();
        employeeListService = new EmployeeListService(employeeObservableList);
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
        refreshEmployeeData();
        setDataToEmployeeTable();
        handleViewRecord();
    }

    public void displayMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("MotorPH HR System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void handleViewRecord() {
        employeeTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                new EmployeeFormController().displayEmployeeDialog(false, selectedEmployee(), employeeListService,true);
            }
        });
    }

    @FXML
    private void handleAddNewRecord() {
        new EmployeeFormController().displayEmployeeDialog(false, null, employeeListService, false);
    }

    @FXML
    private void handleUpdateRecord() {
        Employee selectedEmployee = selectedEmployee();
        if (selectedEmployee != null) {
            new EmployeeFormController().displayEmployeeDialog(true, selectedEmployee, employeeListService, false);
        } else {
            AlertUtil.showNoSelectionAlert("Please select an employee record to update");
        }
    }

    @FXML
    public void handleDeleteRecord() {
        Employee selectedEmployee = selectedEmployee();
        if (selectedEmployee == null) {
            AlertUtil.showNoSelectionAlert("Please select an employee record to delete");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Deletion", "Are you sure you want to delete this employee record? This action cannot be undone.");
        if (confirmed) {

            empDataService.deleteEmployeeRecord(selectedEmployee.getEmployeeID());
            employeeListService.removeEmployee(selectedEmployee);
            refreshEmployeeData();

        }
    }

    @FXML
    public void handleComputePayroll() {

        if (selectedEmployee() == null) {
            AlertUtil.showNoSelectionAlert("Please select an employee record to compute");
            return;
        }

        try {
            new PayrollController().showPayrollForm(selectedEmployee());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleLogout() {
        try {

            boolean confirmed = AlertUtil.showConfirmationAlert(
                    "Confirm Logout",
                    "Are you sure you want to log out?");
            if (confirmed) {
                new LoginController().showLoginStage();
                closeStage();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeStage() {
        Stage mainUI = (Stage) employeeTable.getScene().getWindow();
        mainUI.close();
    }

    private void initializeTableColumns() {

        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tinNoColumn.setCellValueFactory(new PropertyValueFactory<>("tinNumber"));
        sssNoColumn.setCellValueFactory(new PropertyValueFactory<>("sssNumber"));
        philhealthNoColumn.setCellValueFactory(new PropertyValueFactory<>("philhealthNumber"));
        pagibigNoColumn.setCellValueFactory(new PropertyValueFactory<>("pagibigNumber"));

        bindTableColumnsToTableViewWidth();
    }

    private void bindTableColumnsToTableViewWidth() {
        double numOfColumns = employeeTable.getColumns().size();
        for (TableColumn<Employee, ?> column : employeeTable.getColumns()) {
            column.prefWidthProperty().bind(employeeTable.widthProperty().divide(numOfColumns));
        }
    }

    private void refreshEmployeeData() {
        employeeObservableList.setAll(empDataService.getAllEmployees());
    }

    private void setDataToEmployeeTable() {
        employeeTable.setItems(employeeObservableList);
    }

    private Employee selectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

}
