import Controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MotorPHApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            new LoginController().showLoginStage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
