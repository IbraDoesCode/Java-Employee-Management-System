package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import model.Employee;
import model.Mode;
import model.User;
import util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class EmployeeTableController {

    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> employeeIdColumn;
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

    private User user;
    private ObservableList<Employee> employeeObservableList;

    @FXML
    public void initialize() {
        initializeTableColumns();
        setupViewRecordListener();
    }

    public void setUser(User user) {
        this.user = user;
        employeeObservableList = user.getEmpDataService().getEmployeeObservableList();
        setDataToEmployeeTable();
        setupSearchListener();
    }

    @FXML
    public void handleAddNewRecord() {
        initializeEmployeeForm(user, Mode.ADD, null);
    }

    @FXML
    public void handleUpdateRecord() {
        Employee selectedEmployee = selectedEmployee();

        if (selectedEmployee != null) {
            initializeEmployeeForm(user, Mode.UPDATE, selectedEmployee);
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
            user.deleteEmployee(selectedEmployee.getEmployeeId());
        }
    }

    @FXML
    public void handleComputePayroll() {

        if (selectedEmployee() == null) {
            AlertUtil.showNoSelectionAlert("Please select an employee record to compute");
            return;
        }

        try {
            new PayrollController().displayPayrollStage(selectedEmployee());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleLogout() {

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Logout", "Are you sure you want to log out?");
        if (confirmed) {
            closeMainUI();
        }
    }

    private void setupViewRecordListener() {
        Employee employee = selectedEmployee();
        employeeTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                initializeEmployeeForm(user, Mode.VIEW, employee);
            }
        });
    }

    private void setupSearchListener() {
        FilteredList<Employee> filteredData = new FilteredList<>(employeeObservableList, b -> true);

        searchTextField.textProperty().addListener(((_, _, newValue) -> {

            filteredData.setPredicate(employee -> {

                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (String.valueOf(employee.getEmployeeId()).equals(searchKeyword)) {
                    return true;
                } else if (employee.getPersonalInfo().getLastName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.getPersonalInfo().getFirstName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.getGovernmentIds().getTinNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.getGovernmentIds().getSssNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.getGovernmentIds().getPagibigNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else return employee.getGovernmentIds().getPhilhealthNumber().toLowerCase().contains(searchKeyword);

            });

        }));

        SortedList<Employee> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(employeeTable.comparatorProperty());

        employeeTable.setItems(sortedData);

    }

    private void closeMainUI() {
        Stage mainUI = (Stage) employeeTable.getScene().getWindow();
        mainUI.close();
    }

    private void initializeTableColumns() {

        employeeIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployeeId())));

        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPersonalInfo().getFirstName()));

        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPersonalInfo().getLastName()));

        tinNoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGovernmentIds().getTinNumber()));

        sssNoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGovernmentIds().getSssNumber()));

        philhealthNoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGovernmentIds().getPhilhealthNumber()));

        pagibigNoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGovernmentIds().getPagibigNumber()));

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

    private void initializeEmployeeForm(User user, Mode mode, Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeForm.fxml"));
            DialogPane empDetailsDialogPane = loader.load();

            EmployeeFormController controller = loader.getController();
            controller.setUser(user);
            controller.setMode(mode);


            if (employee != null) {
                controller.setEmployee(employee);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(empDetailsDialogPane);

            if (mode == Mode.VIEW) {
                empDetailsDialogPane.lookupButton(ButtonType.OK).setVisible(false);
                empDetailsDialogPane.lookupButton(ButtonType.CANCEL).setVisible(false);
            }

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                controller.handleSaveRecord();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
