

package main.java.com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AdmindashboardController {

    @FXML
    private StackPane contentArea;



    @FXML
    private void loadForm(String fxmlFile) {
        try {
            URL resource = getClass().getResource(fxmlFile);
            if (resource == null) {
                System.out.println("FXML file not found: " + fxmlFile);
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not find form: " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent form = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(form);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not load form: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    @FXML
    public void goToAddLectureForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/addlecture.fxml");
    }

    @FXML
    public void goToAddStudentForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/addstudent.fxml");
    }

    @FXML
    public void goToAddAcademicYearForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/addAccademicYear.fxml");
    }

    @FXML
    public void goToAddClassForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/addClass.fxml");
    }

    @FXML
    public void goToAddModuleForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/addmodule.fxml");
    }

    @FXML
    public void goToCreateReportForm(ActionEvent event) throws IOException {
        loadForm("/com/example/demo/reporting.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Logout");
        confirmationAlert.setHeaderText("Are you sure you want to log out?");
        confirmationAlert.setContentText("You will be redirected to the login screen.");


        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/login.fxml"));
            Scene scene = new Scene(root);

            // Set the scene on the stage
            stage.setScene(scene);


            stage.setWidth(800.0);
            stage.setHeight(600.0);

            // Center the stage on the screen
            stage.centerOnScreen();

            stage.show();
        } else {
            // User cancelled, do nothing
            confirmationAlert.close();
        }
    }


}
