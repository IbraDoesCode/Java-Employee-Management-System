package controller;

import service.AuthenticationService;
import util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void login() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showMissingCredentialAlert();
            return;
        }

        if (!(authService.authenticate(username, password))) {
            AlertUtil.showInvalidCredentialAlert();
            return;
        }

        AlertUtil.showSuccessfulLoginAlert();
        initializeMainUI();
        closeStage();


    }

    private void initializeMainUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeTable.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("MotorPH HR System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeStage() {
        Stage loginStage = (Stage) usernameTextField.getScene().getWindow();
        loginStage.close();
    }

}
