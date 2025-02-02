package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.controller.FlashMessage;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.repository.SecretaryRepository.StudentRepository;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.DossierSingleton;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddStudentsController {

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField addressField;

    @FXML
    public  TextField diplomeField;

    @FXML
    private Label label;

    // ✅ Validation d'un email avec une regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // ✅ Validation d'un numéro de téléphone (10 chiffres FR ou format international)
    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
        return phone.matches(phoneRegex);
    }


    @FXML
    public void addStudent(ActionEvent event) throws IOException, SQLException {
        if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty() ||
                diplomeField.getText().isEmpty()) {
            return;
        }

        // Vérifier le format de l'email
        if (!isValidEmail(emailField.getText())) {
            showFlashMessage("Adresse e-mail invalide !");
            return;
        }

        // Vérifier le format du numéro de téléphone
        if (!isValidPhoneNumber(phoneField.getText())) {
            showFlashMessage("Numéro de téléphone invalide !");
            return;
        }


        try {
            Integer.parseInt(phoneField.getText());
        } catch (NumberFormatException e) {
            showFlashMessage("Le numéro de téléphone doit être un entier !");
            return;
        }
        Student student = new Student(0, lastNameField.getText(), firstNameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()), addressField.getText(), diplomeField.getText());
        StudentRepository.register(student);
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

    private void showFlashMessage(String message) {
        Platform.runLater(() -> {
            label.setText(message);

            // Planifie l'effacement du message après 3 secondes
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), evt -> label.setText(""))
            );
            timeline.setCycleCount(1); // Exécute une seule fois
            timeline.play();
        });
    }


    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }

    @FXML
    public void changePageSceneHome(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

    @FXML
    public void changePageSceneDossierInscription(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/allStudents.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changePageSceneFichesEtudiantes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/allFichesStudents-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changePageSceneAddStudent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/addStudents.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
