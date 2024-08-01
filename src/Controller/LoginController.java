package Controller;

import Service.AuthenticationService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private final AuthenticationService authService;

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;

    public LoginController() {
        this.authService = new AuthenticationService();
    }

    public void displayLoginStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("MotorPH Login Portal");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void login() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {

            if (username.isEmpty() || password.isEmpty()) {
                AlertUtil.showMissingCredentialAlert();
                return;
            }

            if (!(authService.authenticate(username, password))) {
                AlertUtil.showInvalidCredentialAlert();
                return;
            }

            AlertUtil.showSuccessfulLoginAlert();
            new EmployeeListController().displayMainStage();
            closeStage();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

    }

    private void closeStage() {
        Stage loginStage = (Stage) usernameTextField.getScene().getWindow();
        loginStage.close();
    }

}
