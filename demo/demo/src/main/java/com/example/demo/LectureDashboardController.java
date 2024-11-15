package main.java.com.example.demo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ResourceBundle;

public class LectureDashboardController implements Initializable {

    @FXML
    private ListView<HBox> studentListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateStudentList();
    }

    // Method to populate the ListView with students from the database
    private void populateStudentList() {
        String query = "SELECT student_name FROM students";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Clear existing items in case this is called multiple times
            studentListView.getItems().clear();

            while (resultSet.next()) {
                // Get student name from the result set
                String studentName = resultSet.getString("student_name");

                // Create a label for the student name
                Label nameLabel = new Label(studentName);
                nameLabel.setStyle("-fx-font-size: 14px;");

                // Create a checkbox for marking attendance
                CheckBox attendanceCheckBox = new CheckBox();

                // Create an HBox to hold both the label and checkbox
                HBox studentItem = new HBox(10, nameLabel, attendanceCheckBox);

                // Add the HBox to the ListView
                studentListView.getItems().add(studentItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load students from the database.");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Logout");
        confirmationAlert.setHeaderText("Are you sure you want to log out?");
        confirmationAlert.setContentText("You will be redirected to the login screen.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(800.0);
            stage.setHeight(500.0);
            stage.centerOnScreen();
            stage.show();
        } else {
            confirmationAlert.close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
