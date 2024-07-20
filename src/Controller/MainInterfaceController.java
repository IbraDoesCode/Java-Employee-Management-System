package Controller;

import Model.Employee;
import Service.EmployeeDataService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class MainInterfaceController {

    EmployeeDataService empDataService = new EmployeeDataService();

    @FXML
    private StackPane stackPane;

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

    @FXML
    public void initialize() {
        initializeTableColumns();
        initializeTableView();
        loadEmployeeData();
    }

    @FXML
    private void showAddEmployeeForm() {
        new AddEmployeeController().showAddNewEmployeeStage();
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
        // Bind the width of each column to a proportion of the TableView's width
        employeeTable.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
                double width = newWidth.doubleValue() / employeeTable.getColumns().size();
                for (TableColumn<?, ?> column : employeeTable.getColumns()) {
                    column.setPrefWidth(width);
                }
            }
        });
    }

    private void initializeTableView() {
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tinNoColumn.setCellValueFactory(new PropertyValueFactory<>("tinNumber"));
        sssNoColumn.setCellValueFactory(new PropertyValueFactory<>("sssNumber"));
        philhealthNoColumn.setCellValueFactory(new PropertyValueFactory<>("philhealthNumber"));
        pagibigNoColumn.setCellValueFactory(new PropertyValueFactory<>("pagibigNumber"));
    }

    private void loadEmployeeData() {
        try {
            List<Employee> employeeList = empDataService.retrieveEmployeeData();
            ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(employeeList);
            employeeTable.setItems(employeeObservableList);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private Employee selectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void onTerminateEmployeeRecord() {

        int id = selectedEmployee().getEmployeeID();

        boolean confirmed = AlertUtil.showConfirmationAlert(
                "Confirm Deletion",
                "Are you sure you want to delete this employee record? This action cannot be undone.");

        if (confirmed) {
            try {
                empDataService.deleteEmployeeRecord(id);
                loadEmployeeData();
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Record Deletion", "Record deleted successfully");
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
