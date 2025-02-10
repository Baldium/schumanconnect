package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.repository.SecretaryRepository.DossierRepository;
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

public class EditDossierStudentController implements Initializable {

    @FXML
    private Label getNameLabel;

    @FXML
    private Label getStudentNameLastName;


    @FXML
    private TextArea motivationField;

    @FXML
    private ComboBox<String> filiereComboBox;

    @FXML
    private RadioButton etatEnAttente;

    @FXML
    private RadioButton etatRefuse;

    @FXML
    private RadioButton etatAccepte;

    @FXML
    private Label messageErreur;

    Dossier dossierId = DossierSingleton.getInstance().getDossierId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection my_bdd = Bdd.my_bdd();
            PreparedStatement reqGetStudent = my_bdd.prepareStatement(
                    "SELECT e.*, d.* FROM `dossiers` AS d INNER JOIN etudiants AS e ON e.id_etudiant = d.ref_etudiant WHERE id_dossier = ?");
            reqGetStudent.setInt(1, dossierId.getIdDossier());
            ResultSet data = reqGetStudent.executeQuery();



            if (data.next()) {
                // 📌 Pré-remplissage des labels nom/prénom
                getNameLabel.setText("Dossier de " + data.getString("prenom"));
                getStudentNameLastName.setText(data.getString("nom") + " " + data.getString("prenom") + " :");

                // 📌 Pré-remplissage des champs
                motivationField.setText(data.getString("motiv_etudiant"));
                filiereComboBox.setValue(data.getString("filiere_interet")); // Sélectionner la filière actuelle

                // 🎯 Sélection automatique du bon état du dossier (0 = refusé, 1 = accepté, 2 = en attente)
                int etatDossier = data.getInt("statut");
                if (etatDossier == 2) {
                    etatEnAttente.setSelected(true);
                } else if (etatDossier == 0) {
                    etatRefuse.setSelected(true);
                } else if (etatDossier == 1) {
                    etatAccepte.setSelected(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Gérer la sélection unique manuellement
        etatEnAttente.setOnAction(event -> {
            etatRefuse.setSelected(false);
            etatAccepte.setSelected(false);
        });

        etatRefuse.setOnAction(event -> {
            etatEnAttente.setSelected(false);
            etatAccepte.setSelected(false);
        });

        etatAccepte.setOnAction(event -> {
            etatEnAttente.setSelected(false);
            etatRefuse.setSelected(false);
        });

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
    public void editDossier(ActionEvent event) {
        try {
            if (filiereComboBox.getValue() == null || motivationField.getText().trim().isEmpty() ||
                    (!etatEnAttente.isSelected() && !etatAccepte.isSelected() && !etatRefuse.isSelected())) {
                showFlashMessage("⚠️ Veuillez remplir tous les champs avant de soumettre !");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Modification du dossier");
            alert.setContentText("Êtes-vous sûr de vouloir modifier ce dossier ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Récupération des données modifiées
                String newFiliere = filiereComboBox.getValue();
                String newMotivation = motivationField.getText();

                // Vérifier quel bouton radio est sélectionné et convertir en TINYINT
                String newStatut = "2";  // Par défaut "En attente"
                if (etatRefuse.isSelected()) newStatut = "0";
                else if (etatAccepte.isSelected()) newStatut = "1";

                // Mettre à jour le dossier en base de données
                DossierRepository.updateDossier(dossierId.getIdDossier(), newFiliere, newMotivation, newStatut);

                // Message de succès et redirection
                showFlashMessage("Le dossier a été mis à jour avec succès !");
                ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml", event);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showFlashMessage("Erreur lors de la mise à jour du dossier !");
        }
    }

    @FXML
    public void cancelEdit(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
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
