package main.java.com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        URL fxml = HelloApplication.class.getResource("/com/example/demo/login.fxml");

        // Check if the FXML path is found
        if (fxml == null) {
            throw new IOException("FXML file not found at specified path.");
        } else {
            System.out.println("FXML Path: " + fxml);
        }

        // Load the FXML into the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);

        // Set up the Scene and Stage
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
