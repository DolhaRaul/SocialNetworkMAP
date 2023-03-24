package uifx;

import controller.DataController;
import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        /**
         * aici avem "fereastra de inceput", cea cand utilizatorul incearca sa se logheze
         */
        LogInController controller = fxmlLoader.getController();
        controller.setController(new DataController());
        controller.setStage(stage);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}