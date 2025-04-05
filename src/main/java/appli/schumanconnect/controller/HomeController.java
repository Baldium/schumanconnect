package appli.schumanconnect.controller;

import appli.schumanconnect.model.User;
import appli.schumanconnect.repository.HomeRepository;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label labelName;

    @FXML
    private Label label_dossierr_finsih;

    @FXML
    private Label label_dossierr_wait;

    @FXML
    private Label label_dossierr_wait1;

    @FXML
    private Label progressBarFinish;

    @FXML
    private Label progressBarWait;

    @FXML
    private Label progressBarReject;

    @FXML
    private ProgressBar progressBarFinish2;

    @FXML
    private ProgressBar progressBarAttente2;

    @FXML
    private ProgressBar progressBarReject2;


    UserConnectedSingleton UserConnected = UserConnectedSingleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = UserConnected.getUserConnected();
        if (user != null)
            labelName.setText("Bienvenue " + user.getPrenom() + " " + user.getNom() + " !");
        else
            labelName.setText("Bienvenue !");

        // Le nombre de dossier fini, en attente et rejetes
        try {
            label_dossierr_finsih.setText(String.valueOf(HomeRepository.nbDossierTermine()));
            label_dossierr_wait.setText(String.valueOf(HomeRepository.nbDossierAttente()));
            label_dossierr_wait1.setText(String.valueOf(HomeRepository.nbDossierRejete()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Les statistiques
        try {
            double pourcentageTermine = HomeRepository.pourcentageDossierTermine();
            double pourcentageAttente = HomeRepository.pourcentageDossierAttente();
            double pourcentageRejete = HomeRepository.pourcentageDossierRejete();

            progressBarFinish.setText("Complété : " + String.format("%d", (int) pourcentageTermine) + "%");
            progressBarWait.setText("Complété : " + String.format("%d", (int) pourcentageAttente) + "%");
            progressBarReject.setText("Complété : " + String.format("%d", (int) pourcentageRejete) + "%");


            progressBarFinish2.setProgress(pourcentageTermine / 100.0);
            progressBarAttente2.setProgress(pourcentageAttente / 100.0);
            progressBarReject2.setProgress(pourcentageRejete / 100.0);

        } catch (SQLException e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void changePageSceneHome(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
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
    public void changePageSceneDossierInscriptionButton(ActionEvent event) throws IOException{
        ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml",event);
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
    public void changePageSceneAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/adminView/home-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changePageSceneRdv(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/rdvView/dossierEtudiant-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public void changePageSceneAddFurniture(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/furnitureView/createFurniture-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public void changePageSceneE5(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/pageE5-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public void changePageSceneManagementFurniture(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/furnitureView/ManagementFurniture-view.fxml"));
        Parent root = loader.load();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }




}
