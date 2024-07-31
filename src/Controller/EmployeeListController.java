package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Util.AlertUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class EmployeeListController {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;
    @FXML
    private TableColumn<Employee, Integer> firstNameColumn;
    @FXML
    private TableColumn<Employee, Integer> lastNameColumn;
    @FXML
    private TableColumn<Employee, Integer> tinNoColumn;
    @FXML
    private TableColumn<Employee,Integer> sssNoColumn;
    @FXML
    private TableColumn<Employee,Integer> philhealthNoColumn;
    @FXML
    private TableColumn<Employee,Integer> pagibigNoColumn;

    private final EmployeeDataService empDataService;
    private final ObservableList<Employee> employeeObservableList;

    public EmployeeListController() {
        empDataService = new EmployeeDataService();
        employeeObservableList = empDataService.getEmployeeObservableList();
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
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
                new EmployeeFormController().displayEmployeeDialog(false, selectedEmployee(), empDataService, true);
            }
        });
    }

    @FXML
    private void handleAddNewRecord() {
        new EmployeeFormController().displayEmployeeDialog(false, null, empDataService, false);
    }

    @FXML
    private void handleUpdateRecord() {
        Employee selectedEmployee = selectedEmployee();

        if (selectedEmployee != null) {
            new EmployeeFormController().displayEmployeeDialog(true, selectedEmployee, empDataService, false);
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
            boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Logout", "Are you sure you want to log out?");
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

    private void setDataToEmployeeTable() {
        employeeTable.setItems(employeeObservableList);
    }

    private Employee selectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

}
