package appli.schumanconnect.controller.SecretaryController;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.repository.SecretaryRepository.DossierRepository;
import appli.schumanconnect.repository.SecretaryRepository.StudentRepository;
import appli.schumanconnect.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ViewDossierController implements Initializable {

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
    private Label filiereLabel;

    @FXML
    private Label motivationLabel;

    @FXML
    private Label etatDossierLabel;

    Dossier dossierId = DossierSingleton.getInstance().getDossierId();
    Student StudentId = StudentSingleton.getInstance().getStudentId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ResultSet data = DossierRepository.getDossierStudent(dossierId);
            while (data.next()){
                getNameLabel.setText("Dossier de " + data.getString("prenom"));
                getStudentNameLastName.setText(data.getString("nom") +" " + data.getString("prenom"));
                emailLabel.setText(data.getString("email"));
                telephoneLabel.setText(data.getString("tel"));
                adresseLabel.setText(data.getString("adresse"));
                diplomeLabel.setText(data.getString("dernier_diplome_obtenu"));
                filiereLabel.setText(data.getString("filiere_interet"));
                motivationLabel.setText(data.getString("motiv_etudiant"));
                String statutDossierParse = data.getString("statut");
                if (Objects.equals(statutDossierParse, "1"))
                    statutDossierParse = "Dossier accepté";
                else if (Objects.equals(statutDossierParse, "2"))
                    statutDossierParse = "Dossier en attente";
                else
                    statutDossierParse = "Dossier refusé";
                etatDossierLabel.setText(statutDossierParse);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void accesStudent(ActionEvent event) throws SQLException, IOException {
        StudentSingleton.getInstance().setStudentId(StudentId);
        ScenePage.switchView("/appli/schumanconnect/secretaryView/ficheStudent-view.fxml", event);
    }

    @FXML
    public void exportPdf() {
        try {
            // Nom du fichier PDF
            String fileName = "Dossier_Etudiant_" + getStudentNameLastName.getText() + ".pdf";

            // Création du document
            Document document = new Document();

            // Création du flux de sortie pour enregistrer le PDF
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // Ouvrir le document
            document.open();

            // Ajouter un titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Dossier de l'Étudiant", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espacement après le titre
            document.add(Chunk.NEWLINE);

            // Définir la police pour le contenu
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Ajouter les informations du dossier
            document.add(new Paragraph("Nom et Prénom: " + getStudentNameLastName.getText(), contentFont));
            document.add(new Paragraph("Email: " + emailLabel.getText(), contentFont));
            document.add(new Paragraph("Téléphone: " + telephoneLabel.getText(), contentFont));
            document.add(new Paragraph("Adresse: " + adresseLabel.getText(), contentFont));
            document.add(new Paragraph("Diplôme: " + diplomeLabel.getText(), contentFont));
            document.add(new Paragraph("Filière: " + filiereLabel.getText(), contentFont));
            document.add(new Paragraph("Motivation: " + motivationLabel.getText(), contentFont));
            document.add(new Paragraph("État du Dossier: " + etatDossierLabel.getText(), contentFont));

            // Fermer le document
            document.close();

            // Affichage d'une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Le PDF a été exporté avec succès : " + fileName, ButtonType.OK);
            alert.setTitle("Exportation réussie");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (Exception e) {
            // Alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'exportation du PDF. Veuillez réessayer.", ButtonType.OK);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    public void editDossier(ActionEvent event) throws IOException{
        DossierSingleton.getInstance().setDossierId(dossierId);
        ScenePage.switchView("/appli/schumanconnect/secretaryView/editDossierStudent-view.fxml", event);
    }

    @FXML
    public void dropDossier(ActionEvent event) throws IOException, SQLException {
        DossierRepository.dropDossier(dossierId);
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
