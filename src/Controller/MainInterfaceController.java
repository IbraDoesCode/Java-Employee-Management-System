package Controller;

import Model.Employee;
import Service.EmployeeRecordService;
import Service.ObservableListService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainInterfaceController {

    private final EmployeeRecordService empDataService;

    private final ObservableList<Employee> employeeObservableList;

    private final ObservableListService observableListService;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn, lastNameColumn, tinNoColumn, sssNoColumn, philhealthNoColumn, pagibigNoColumn;

    public MainInterfaceController() {
        empDataService = new EmployeeRecordService();
        employeeObservableList = FXCollections.observableArrayList();
        observableListService = new ObservableListService(employeeObservableList);
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
        refreshEmployeeData();
        setDataToEmployeeTable();

    }

    public void initializeMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/MainInterface.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("MotorPH HR System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleAddNewRecord() {
        new EmployeeFormController().showEmployeeForm(false, null, observableListService);
    }

    @FXML
    private void handleUpdateRecord() {
        Employee selectedEmployee = selectedEmployee();
        if (selectedEmployee != null) {
            new EmployeeFormController().showEmployeeForm(true, selectedEmployee, observableListService);
        } else {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update.");
        }
    }

    @FXML
    public void handleDeleteRecord() {
        Employee selectedEmployee = selectedEmployee();
        if (selectedEmployee == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee record to delete.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Deletion", "Are you sure you want to delete this employee record? This action cannot be undone.");
        if (confirmed) {

            empDataService.deleteEmployeeRecord(selectedEmployee.getEmployeeID());
            observableListService.removeEmployee(selectedEmployee);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Record Deletion", "Record deleted successfully");
            refreshEmployeeData();

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

        List<Employee> employeeList = empDataService.retrieveListOfEmployeeObject();
        employeeObservableList.setAll(employeeList);
    }

    private void setDataToEmployeeTable() {
        employeeTable.setItems(employeeObservableList);
    }

    private Employee selectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

}
