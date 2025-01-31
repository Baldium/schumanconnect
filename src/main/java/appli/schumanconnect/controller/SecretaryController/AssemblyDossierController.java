package appli.schumanconnect.controller.SecretaryController;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.repository.SecretaryRepository.DossierRepository;
import appli.schumanconnect.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AssemblyDossierController implements Initializable {

    @FXML
    private Label getNameLabel;

    @FXML
    private Label getStudentNameLastName;

    @FXML
    private Label emailLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label diplomeLabel;

    @FXML
    private ComboBox<String> filiereComboBox;

    @FXML
    private TextArea motivationField;

    @FXML
    private RadioButton etatEnAttente, etatRefuse, etatAccepte;


    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Séparer la date et l'heure en chaînes distinctes
    String dateTime = now.format(dateFormatter) + " " + now.format(timeFormatter);    String time = now.format(timeFormatter);  // Exemple: 14:45:30

    Student StudentId = StudentSingleton.getInstance().getStudentId();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection my_bdd = Bdd.my_bdd();
            PreparedStatement reqGetStudent = my_bdd.prepareStatement("SELECT * FROM `etudiants` WHERE `id_etudiant`= ?");
            reqGetStudent.setInt(1, StudentId.getIdEtudiant());
            ResultSet data = reqGetStudent.executeQuery();

            while (data.next())
            {
                getNameLabel.setText("Creation du dossier de " + data.getString("prenom"));
                getStudentNameLastName.setText(data.getString("nom") +" " + data.getString("prenom") + " :");
                emailLabel.setText(data.getString("email"));
                telephoneLabel.setText(data.getString("tel"));
                adresseLabel.setText(data.getString("adresse"));
                diplomeLabel.setText(data.getString("dernier_diplome_obtenu"));
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
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

    @FXML
    public void addStudent(ActionEvent event) throws SQLException, IOException {
        int etat = -1;
        if (etatEnAttente.isSelected()) {
            etat = 2;  // En attente
        } else if (etatAccepte.isSelected()) {
            etat = 1;  // Accepté
        } else if (etatRefuse.isSelected()) {
            etat = 0;  // Refusé
        }
        Dossier dossier = new Dossier(0, dateTime, filiereComboBox.getValue(), motivationField.getText(), etat, StudentId.getIdEtudiant());
        DossierRepository.addDossierStudent(dossier);

        ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml", event);
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
    public void changePageSceneAddStudent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/addStudents.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changePageSceneAddStudentButton(ActionEvent event) throws IOException{
        ScenePage.switchView("/appli/schumanconnect/secretaryView/addStudents.fxml", event);
    }

}

