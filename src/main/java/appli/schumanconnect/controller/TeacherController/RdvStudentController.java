package appli.schumanconnect.controller.TeacherController;

import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RdvStudentController implements Initializable {

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private DatePicker dateRdv;

    @FXML
    private ComboBox<String> salle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> salleList = FXCollections.observableArrayList();

        try {
            Connection myBdd = Bdd.my_bdd();
            PreparedStatement salleStmt = myBdd.prepareStatement("SELECT * FROM salles");
            ResultSet data = salleStmt.executeQuery();

            while (data.next()) {
                salleList.add(data.getString("nom_salle"));
            }

            salle.setItems(salleList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void confirmRdv(ActionEvent event) throws IOException{


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
    public void changePageSceneHome(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
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