package Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtil {


    public static void showAlert(Alert.AlertType type, String title, String message) {
        showAlert(type, title, message, null);
    }

    public static boolean showConfirmationAlert(String title, String message) {
        return showAlert(Alert.AlertType.CONFIRMATION, title, message, ButtonType.OK) == ButtonType.OK;
    }

    public static void showRecordSavedAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Record Saved", "The employee record has been saved successfully.");
    }

    public static void showRecordUpdatedAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Record updated", "The employee record has been updated successfully.");
    }

    public static void showDuplicateRecordExists() {
        showAlert(Alert.AlertType.ERROR, "Employee Record Exists", "An employee record with the same details already exists. Please verify the information and try again.");
    }

    public static void showIncompleteDataAlert() {
        showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please ensure all required fields are filled out.");
    }

    public static boolean confirmDetails() {
        return showConfirmationAlert("Confirm Details", "Please confirm that all the details are correct before saving.");
    }

    public static void showNoSelectionAlert(String message) {
        showAlert(Alert.AlertType.WARNING, "No Employee Selected", message);
    }

    public static void showRecordDeletedAlert(){
        showAlert(Alert.AlertType.INFORMATION, "Record Deletion", "Record deleted successfully");
    }

    public static void showInvalidCredentialAlert() {
        showAlert(Alert.AlertType.ERROR,"Invalid Credentials","The username or password you entered is incorrect.");
    }

    public static void showMissingCredentialAlert() {
        showAlert(Alert.AlertType.WARNING,"Missing Credentials","Please enter both username and password.");
    }

    public static void showSuccessfulLoginAlert() {
        showAlert(Alert.AlertType.INFORMATION,"Login Successful","You have successfully logged in!");
    }

    private static ButtonType showAlert(Alert.AlertType type, String title, String message, ButtonType defaultButtonType) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);

        if (defaultButtonType != null) {
            alert.getButtonTypes().setAll(defaultButtonType, ButtonType.CANCEL);
        } else {
            alert.getButtonTypes().setAll(ButtonType.OK);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null);
    }
}
