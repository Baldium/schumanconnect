package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AllStudentsController implements Initializable  {

    @FXML
    private TableView<Dossier> DossierTableView;

    @FXML
    private TableColumn<Dossier, Integer> idStudentTableColumn;

    @FXML
    private TableColumn<Dossier, String> nameStudentTableColumn;

    @FXML
    private TableColumn<Dossier, String> firstNameTableColumn;

    @FXML
    private TableColumn<Dossier, String> dateCreationTableColumn;

    @FXML
    private TableColumn<Dossier, String> statusTableColumn;

    @FXML
    private Button seeDossier;

    @FXML
    private Button editDossier;

    @FXML
    private Button deleteDossier;

    ObservableList<Dossier> dossierObservableList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            String req = "SELECT d.*, e.* FROM dossiers as d INNER JOIN etudiants as e on d.ref_etudiant = e.id_etudiant";
            Connection conn = Bdd.my_bdd();
            Statement statement = conn.createStatement();
            ResultSet queryOutput = statement.executeQuery(req);

            while (queryOutput.next()){
                Integer idDossier = queryOutput.getInt("id_dossier");
                String date_creation = queryOutput.getString("date_creation");
                String filiere_interet = queryOutput.getString("filiere_interet");
                String motiv_etudiant = queryOutput.getString("motiv_etudiant");
                Integer status = queryOutput.getInt("statut");
                Integer ref_etudiant = queryOutput.getInt("ref_etudiant");

                dossierObservableList.add(new Dossier(idDossier, date_creation, filiere_interet
                ,motiv_etudiant, status, ref_etudiant));
            }
            idStudentTableColumn.setCellValueFactory(new PropertyValueFactory<>("id_dossier"));
            dateCreationTableColumn.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
            firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("filiere_interet"));
            idStudentTableColumn.setCellValueFactory(new PropertyValueFactory<>("motiv_etudiant"));
            statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
            nameStudentTableColumn.setCellValueFactory(new PropertyValueFactory<>("ref_etudiant"));

            DossierTableView.setItems(dossierObservableList);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }


}
