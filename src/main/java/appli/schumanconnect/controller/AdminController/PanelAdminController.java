package appli.schumanconnect.controller.AdminController;

import appli.schumanconnect.model.Role;
import appli.schumanconnect.model.User;
import appli.schumanconnect.repository.UserRepository;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;


public class PanelAdminController implements Initializable {
    @FXML
    private TableView<User> user_tableView;
    @FXML
    private TableColumn<User,Integer> user_id_column;
    @FXML
    private TableColumn<User,String> user_nom_column;
    @FXML
    private TableColumn<User,String> user_prenom_column;
    @FXML
    private TableColumn<User,String> user_mail_column;
    @FXML
    private TableColumn<User,String> user_role_column;
    @FXML
    private TableColumn<User,String> user_dernierco_column;


    private ObservableList<User> user_list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.user_list = UserRepository.getAllUser();
            this.user_id_column.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            this.user_nom_column.setCellValueFactory(new PropertyValueFactory<>("nom"));
            this.user_prenom_column.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            this.user_mail_column.setCellValueFactory(new PropertyValueFactory<>("email"));
            this.user_role_column.setCellValueFactory(new PropertyValueFactory<>("role"));
            this.user_dernierco_column.setCellValueFactory(new PropertyValueFactory<>("last_log"));

            this.user_tableView.setItems(this.user_list);

        } catch (SQLException e) {
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
    public void changePageSceneAdminAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/adminView/addAdmin.fxml"));
        Parent root = loader.load();
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }



}
