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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainInterfaceController {

    private EmployeeDataService empDataService;
    private ObservableList<Employee> employeeObservableList;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, String> tinNoColumn;

    @FXML
    private TableColumn<Employee, String> sssNoColumn;

    @FXML
    private TableColumn<Employee, String> philhealthNoColumn;

    @FXML
    private TableColumn<Employee, String> pagibigNoColumn;

    public MainInterfaceController() {
        empDataService = new EmployeeDataService();
        employeeObservableList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
        refreshEmployeeData();
        setDataToEmployeeTable();
    }

    @FXML
    private void showAddEmployeeForm() {
        AddEmployeeController addEmployeeController = new AddEmployeeController();
        addEmployeeController.setEmployeeList(employeeObservableList);
        addEmployeeController.showAddNewEmployeeStage();
    }

    public void initializeMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/MainInterface.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("MotorPH HR System");
        stage.setScene(new Scene(root));
        stage.show();
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
        try {
            List<Employee> employeeList = empDataService.retrieveListOfEmployeeObject();
            employeeObservableList.clear();
            employeeObservableList.addAll(employeeList);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDataToEmployeeTable() {
        employeeTable.setItems(employeeObservableList);
    }

    private Employee selectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void handleDeleteRecord() {

        if (selectedEmployee() == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee to terminate.");
            return;
        }

        int id = selectedEmployee().getEmployeeID();

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Deletion", "Are you sure you want to delete this employee record? This action cannot be undone.");

        if (confirmed) {
            try {
                empDataService.deleteEmployeeRecord(id);
                refreshEmployeeData();
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Record Deletion", "Record deleted successfully");
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
