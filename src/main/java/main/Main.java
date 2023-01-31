package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/mainView.fxml")));
        stage.setTitle("Car Crime Game");
        stage.setScene(new Scene(root, 1280, 725));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
