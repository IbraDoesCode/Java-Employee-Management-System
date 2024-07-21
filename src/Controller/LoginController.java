package Controller;

import Service.AuthenticationService;
import Util.AlertUtil;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    public void showLoginStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Pages/Login.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("MotorPH Login Portal");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void login() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {

            if (username.isEmpty() || password.isEmpty()) {
                AlertUtil.showAlert(Alert.AlertType.WARNING,
                        "Missing Credentials",
                        "Please enter both username and password.");

                return;
            }

            if (!(authService.authenticate(username, password))) {
                AlertUtil.showAlert(Alert.AlertType.ERROR,
                        "Invalid Credentials",
                        "The username or password you entered is incorrect.");
                return;
            }

            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    "Login Successful",
                    "You have successfully logged in!");
            new MainInterfaceController().initializeMainUI();
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
