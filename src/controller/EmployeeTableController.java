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
    private User user;
    private ObservableList<Employee> employeeList;

    @FXML
    public void initialize() {
        initializeTableColumns();
        setupViewRecordListener();
    }

    public void setUser(User user) {
        this.user = user;
        employeeList = user.getEmpDataService().getEmployeeList();
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

        if (selectedEmployee() == null) {
            AlertUtil.showNoSelectionAlert("Please select an employee record to delete");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Deletion", "Are you sure you want to delete this employee record? This action cannot be undone.");

        if (confirmed) {
            user.deleteEmployee(selectedEmployee().employeeId());
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
        employeeTable.setOnMouseClicked((MouseEvent event) -> {
            Employee employee = selectedEmployee();
            if (event.getClickCount() == 2) {
                initializeEmployeeForm(user, Mode.VIEW, employee);
            }
        });
    }

    private void setupSearchListener() {
        FilteredList<Employee> filteredData = new FilteredList<>(employeeList, _ -> true);

        searchTextField.textProperty().addListener(((_, _, newValue) -> {

            filteredData.setPredicate(employee -> {

                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (String.valueOf(employee.employeeId()).equals(searchKeyword)) {
                    return true;
                } else if (employee.lastName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.firstName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.tinNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.sssNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (employee.pagibigNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else return employee.philhealthNumber().toLowerCase().contains(searchKeyword);

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
        TableColumn<Employee, String> employeeIdColumn = new TableColumn<>("Employee #");
        employeeIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().employeeId())));

        TableColumn<Employee, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().firstName()));

        TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().lastName()));

        TableColumn<Employee, String> tinNoColumn = new TableColumn<>("Tin #");
        tinNoColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().tinNumber()));

        TableColumn<Employee, String> sssNoColumn = new TableColumn<>("SSS #");
        sssNoColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().sssNumber()));

        TableColumn<Employee, String> philhealthNoColumn = new TableColumn<>("Philhealth #");
        philhealthNoColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().philhealthNumber()));

        TableColumn<Employee, String> pagibigNoColumn = new TableColumn<>("Pagibig #");
        pagibigNoColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().pagibigNumber()));

        employeeTable.getColumns().addAll(employeeIdColumn, firstNameColumn, lastNameColumn, tinNoColumn, sssNoColumn, philhealthNoColumn, pagibigNoColumn);
    }

    private void setDataToEmployeeTable() {
        employeeTable.setItems(employeeList);
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
