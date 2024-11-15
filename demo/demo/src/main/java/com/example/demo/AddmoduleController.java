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

public class AddmoduleController implements Initializable {

    @FXML
    private TextField moduleNameField;

    @FXML
    private TextField moduleCodeField;
    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBox with example values
        semesterComboBox.getItems().addAll("Semester 1", "Semester 2");
        yearComboBox.getItems().addAll("Year 1", "Year 2", "Year 3");
        System.out.println("AddModuleController initialized");
    }

    @FXML
    private void handleAddModule() {
        String moduleName = moduleNameField.getText().trim();
        String moduleCode = moduleCodeField.getText().trim();
        String selectedSemester = semesterComboBox.getValue();
        String selectedYear = yearComboBox.getValue();

        // Validate inputs
        if (moduleName.isEmpty() || moduleCode.isEmpty() || selectedSemester == null || selectedYear == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the module to the database
        addModuleToDatabase(moduleName, moduleCode, selectedSemester, selectedYear);

        // Show success message
        showAlert("Success", "Module added successfully: " + moduleName);

        // Clear fields after adding
        moduleNameField.clear();
        moduleCodeField.clear();
        semesterComboBox.getSelectionModel().clearSelection();
        yearComboBox.getSelectionModel().clearSelection();
    }

    private void addModuleToDatabase(String moduleName, String moduleCode, String semester, String year) {
        String insertQuery = "INSERT INTO modules (module_name, module_code, semester, year) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, moduleName);
            preparedStatement.setString(2, moduleCode);
            preparedStatement.setString(3, semester);
            preparedStatement.setString(4, year);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Module added to database.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error adding module to the database.");
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
