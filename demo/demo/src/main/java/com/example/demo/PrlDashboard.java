package main.java.com.example.demo;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class PrlDashboard implements Initializable {

    @FXML
    private ComboBox<String> classComboBox; // ComboBox for selecting class
    @FXML
    private ComboBox<String> moduleComboBox; // ComboBox for selecting module
    @FXML
    private TextArea challengesTextArea; // TextArea for challenges
    @FXML
    private TextArea recommendationsTextArea; // TextArea for recommendations
    @FXML
    private Button saveReportButton; // Button to save report
    @FXML
    private Button clearFormButton; // Button to clear form
    @FXML
    private Label statusMessageLabel; // Label for status messages

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        classComboBox.getItems().addAll("BSBT", "BSSM", "BIT");
        moduleComboBox.getItems().addAll("Java", "databse", "Network");
        //System.out.println("ReportController initialized");
    }

    @FXML
    private void handleSaveReport() {
        String selectedClass = classComboBox.getValue();
        String selectedModule = moduleComboBox.getValue();
        String challenges = challengesTextArea.getText().trim();
        String recommendations = recommendationsTextArea.getText().trim();

        // Validate inputs
        if (selectedClass == null || selectedModule == null || challenges.isEmpty() || recommendations.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the report to the database
        addReportToDatabase(selectedClass, selectedModule, challenges, recommendations);

        // Show success message
        showAlert("Success", "Report saved successfully.");

        // Clear the form after saving
        clearForm();
    }

    private void addReportToDatabase(String selectedClass, String selectedModule, String challenges, String recommendations) {
        String insertQuery = "INSERT INTO reports (class, module, challenges, recommendations) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, selectedClass);
            preparedStatement.setString(2, selectedModule);
            preparedStatement.setString(3, challenges);
            preparedStatement.setString(4, recommendations);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Report added to database.");
            statusMessageLabel.setText("Report saved successfully.");
            statusMessageLabel.setStyle("-fx-text-fill: green;");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error saving report to the database.");
            statusMessageLabel.setText("Error saving report.");
            statusMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private void clearForm() {
        classComboBox.getSelectionModel().clearSelection();
        moduleComboBox.getSelectionModel().clearSelection();
        challengesTextArea.clear();
        recommendationsTextArea.clear();
        statusMessageLabel.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Logout");
        confirmationAlert.setHeaderText("Are you sure you want to log out?");
        confirmationAlert.setContentText("You will be redirected to the login screen.");

        // Show the alert and wait for a response
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed to logout
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root);

            // Set the scene on the stage
            stage.setScene(scene);


            stage.setWidth(800.0);
            stage.setHeight(700.0);

            // Center the stage on the screen
            stage.centerOnScreen();

            stage.show();
        } else {
            // User cancelled, do nothing
            confirmationAlert.close();
        }
    }

}

