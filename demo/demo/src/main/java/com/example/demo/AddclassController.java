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

public class AddclassController implements Initializable {

    @FXML
    private TextField classNameField; // Field for class name
    @FXML
    private ComboBox<String> semesterComboBox; // ComboBox for selecting semester
    @FXML
    private ComboBox<String> academicYearComboBox; // ComboBox for selecting academic year
    @FXML
    private Button addButton; // Button for adding class

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize semesterComboBox and academicYearComboBox with example values
        semesterComboBox.getItems().addAll("Semester 1", "Semester 2");
        academicYearComboBox.getItems().addAll("year 1", "year 2", "year 3");
        System.out.println("AddclassController initialized");
    }

    @FXML
    private void handleAddClass() {
        String className = classNameField.getText().trim();
        String selectedSemester = semesterComboBox.getValue();
        String selectedAcademicYear = academicYearComboBox.getValue();

        // Validate inputs
        if (className.isEmpty() || selectedSemester == null || selectedAcademicYear == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the class to the database
        addClassToDatabase(className, selectedSemester, selectedAcademicYear);

        // Show success message
        showAlert("Success", "Class added successfully: " + className);

        // Clear fields after adding
        classNameField.clear();
        semesterComboBox.getSelectionModel().clearSelection();
        academicYearComboBox.getSelectionModel().clearSelection();
    }

    private void addClassToDatabase(String className, String semester, String academicYear) {
        String insertQuery = "INSERT INTO classes (class_name, semester, academic_year) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, className);
            preparedStatement.setString(2, semester);
            preparedStatement.setString(3, academicYear);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Class added to database.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error adding class to the database.");
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

