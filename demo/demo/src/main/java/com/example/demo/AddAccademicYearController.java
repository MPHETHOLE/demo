package main.java.com.example.demo;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import main.java.com.example.demo.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class AddAccademicYearController implements Initializable {

    @FXML
    private ComboBox<String> yearComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearComboBox.getItems().addAll("2022", "2023", "2024", "2025");
    }


    @FXML
    private void handleAddYear() {
        String selectedYear = yearComboBox.getValue();

        // Validate input
        if (selectedYear == null || selectedYear.isEmpty()) {
            showAlert("Error", "Please select an academic year.");
            return;
        }

        // Add the selected year to the database
        addYearToDatabase(selectedYear);

        // Show success message
        showAlert("Success", "Academic year added successfully: " + selectedYear);

        // Clear selection after adding
        yearComboBox.getSelectionModel().clearSelection();
    }

    private void addYearToDatabase(String year) {
        String insertQuery = "INSERT INTO year (accademicyear) VALUES (?)";


        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, year);

            // Execute the query
            preparedStatement.executeUpdate();
            System.out.println("Academic year added to database.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error adding academic year to the database.");
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

