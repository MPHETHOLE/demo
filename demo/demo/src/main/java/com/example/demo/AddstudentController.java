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

public class AddstudentController implements Initializable {

    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> classComboBox;
    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBox items
        classComboBox.getItems().addAll("BSCIT", "BSSM", "BIT");
        System.out.println("AddController initialized");
    }

    @FXML
    private void handleAddStudent() {
        String studentName = studentNameField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String email = emailField.getText().trim();
        String selectedClass = classComboBox.getValue();

        // Validate inputs
        if (studentName.isEmpty() || studentId.isEmpty() || email.isEmpty() || selectedClass == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the student to the database
        addStudentToDatabase(studentName, studentId, email, selectedClass);

        // Show success message
        showAlert("Success", "Student added successfully: " + studentName);

        // Clear fields
        studentNameField.clear();
        studentIdField.clear();
        emailField.clear();
        classComboBox.getSelectionModel().clearSelection();
    }

    private void addStudentToDatabase(String studentName, String studentId, String email, String studentClass) {
        String insertQuery = "INSERT INTO students (student_name, student_id, email, class) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, studentId);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, studentClass);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Student added to database.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error adding student to the database.");
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

