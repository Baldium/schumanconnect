package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.model.Student;
import appli.schumanconnect.repository.SecretaryRepository.StudentRepository;
import appli.schumanconnect.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditStudentController implements Initializable {

    @FXML
    private Label getNameLabel;

    @FXML
    private Label getStudentNameLastName;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField adresseField;

    @FXML
    private TextField diplomeField;

    @FXML
    private Label messageErreur;

    Student student = StudentSingleton.getInstance().getStudentId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection my_bdd = Bdd.my_bdd();
            PreparedStatement reqGetStudent = my_bdd.prepareStatement("SELECT * FROM etudiants WHERE id_etudiant = ?");
            reqGetStudent.setInt(1, student.getIdEtudiant());
            ResultSet data = reqGetStudent.executeQuery();

            if (data.next()) {
                getNameLabel.setText("Fiche étudiante de " + data.getString("prenom"));
                getStudentNameLastName.setText(data.getString("nom") + " " + data.getString("prenom") + " :");
                emailField.setText(data.getString("email"));
                telephoneField.setText(data.getString("tel"));
                adresseField.setText(data.getString("adresse"));
                diplomeField.setText(data.getString("dernier_diplome_obtenu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showFlashMessage(String message) {
        Platform.runLater(() -> {
            messageErreur.setText(message);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), evt -> messageErreur.setText(""))
            );
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    @FXML
    public void editStudent(ActionEvent event) {
        try {
            if (validateFields()) {
                // Demander confirmation avant mise à jour
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Modification des informations");
                alert.setContentText("Êtes-vous sûr de vouloir modifier ces informations ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    StudentRepository.updateStudent(student.getIdEtudiant(),emailField.getText(), telephoneField.getText(), adresseField.getText(), diplomeField.getText());
                    showFlashMessage("Les informations ont été mises à jour avec succès !");
                    ScenePage.switchView("/appli/schumanconnect/secretaryView/allFichesStudents-view.fxml", event);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showFlashMessage("Erreur lors de la mise à jour !");
        }
    }

    @FXML
    public void cancelEdit(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/secretaryView/allFichesStudents-view.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }

    private boolean validateFields() {
        if (emailField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                adresseField.getText().isEmpty() || diplomeField.getText().isEmpty()) {
            showFlashMessage("Tous les champs doivent être remplis !");
            return false;
        }

        if (!emailField.getText().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
            showFlashMessage("Veuillez entrer une adresse email valide !");
            return false;
        }

        if (!telephoneField.getText().matches("^\\+?\\d{10,15}$")) {
            showFlashMessage("Veuillez entrer un numéro de téléphone valide !");
            return false;
        }

        return true;
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
