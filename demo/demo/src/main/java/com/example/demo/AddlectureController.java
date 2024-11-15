package main.java.com.example.demo;



import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddlectureController implements Initializable {

    @FXML
    private TextField lecturerNameField; // Field for lecturer name
    @FXML
    private TextField employeeNumberField; // Field for employee number
    @FXML
    private TextField emailField; // Field for lecturer email
    @FXML
    private ComboBox<String> roleComboBox; // ComboBox for selecting role
    @FXML
    private Button addButton; // Button for adding a lecturer

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        roleComboBox.getItems().addAll("Lecture", "Principal Lecture");

    }

    @FXML
    private void handleAddLecturer() {
        String lecturerName = lecturerNameField.getText().trim();
        String employeeNumber = employeeNumberField.getText().trim();
        String email = emailField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        // Validate inputs
        if (lecturerName.isEmpty() || employeeNumber.isEmpty() || email.isEmpty() || selectedRole == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the lecturer to the database
        addLecturerToDatabase(lecturerName, employeeNumber, email, selectedRole);

        // Show success message
        showAlert("Success", "Lecturer added successfully: " + lecturerName);

        // Clear fields after adding
        lecturerNameField.clear();
        employeeNumberField.clear();
        emailField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    private void addLecturerToDatabase(String lecturerName, String employeeNumber, String email, String role) {
        String insertQuery = "INSERT INTO lecturers (lecturer_name, employee_number, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, lecturerName);
            preparedStatement.setString(2, employeeNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, role);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Lecturer added to database.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error adding lecturer to the database.");
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

