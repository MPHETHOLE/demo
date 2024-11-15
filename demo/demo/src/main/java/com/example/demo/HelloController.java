package main.java.com.example.demo;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField employeeNumberField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String lecturer_name = usernameField.getText();
        String employee_number = employeeNumberField.getText();

        if (lecturer_name.isEmpty() || employee_number.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your username and employee number.");
            return;
        }

        authenticateUser(lecturer_name, employee_number);
    }

    private void authenticateUser(String lecturer_name, String employee_number) {
        String query = "SELECT role FROM lecturers WHERE lecturer_name = ? AND employee_number = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, lecturer_name);
            preparedStatement.setString(2, employee_number);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");

                // Redirect user to the respective dashboard
                switch (role) {
                    case "Lecture":
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Lecturer! Redirecting to Lecturer Dashboard...");
                        openDashboard("/com/example/demo/lecturedashboard.fxml");
                        break;

                    case "Admin":
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Admin! Redirecting to Admin Dashboard...");
                        openDashboard("/com/example/demo/adminDashboard.fxml");
                        break;

                    case "Principal Lecture":
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome PRL! Redirecting to PRL Dashboard...");
                        openDashboard("/com/example/demo/prlDashboard.fxml");
                        break;

                    default:
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Role not recognized. Please contact support.");
                        break;
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Please try again.");
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred while authenticating user: " + e.getMessage());
            e.printStackTrace();  // This will print the stack trace for better debugging
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
        }
    }

    private void openDashboard(String fxmlFile) {
        try {
            // Attempt to load the FXML file
            URL fxmlUrl = getClass().getResource(fxmlFile);

            if (fxmlUrl == null) {
                // If the FXML file is not found, log the error and show an alert
                System.err.println("FXML file not found: " + fxmlFile);
                showAlert(Alert.AlertType.ERROR, "FXML Error", "FXML file not found: " + fxmlFile);
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close current window if needed
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            System.err.println("Error occurred while loading FXML file: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for debugging
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
