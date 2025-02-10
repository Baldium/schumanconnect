package appli.schumanconnect.controller.TeacherController;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.User;
import appli.schumanconnect.repository.SecretaryRepository.StudentRepository;
import appli.schumanconnect.utils.*;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DossierController implements Initializable {

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
    private TextField keywordTextField;

    ObservableList<Dossier> dossierObservableList = FXCollections.observableArrayList();

    // Code optimisé mais legerement plus complexe pour ne pas avoir de beug lors des recherches et ainsi eviter de surcharger de requetes SQL.
    // Map pour stocker les noms et prénoms
    private Map<Integer, String> studentNames = new HashMap<>();
    private Map<Integer, String> studentFirstNames = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Ici, on fait notre requête pour récupérer toutes les informations nécessaires
            String req = "SELECT d.*, e.* FROM dossiers as d INNER JOIN etudiants as e on d.ref_etudiant = e.id_etudiant where d.statut = 1";
            Connection conn = Bdd.my_bdd();
            Statement statement = conn.createStatement();
            ResultSet queryOutput = statement.executeQuery(req);

            while (queryOutput.next()) {
                Integer idDossier = queryOutput.getInt("id_dossier");
                String date_creation = queryOutput.getString("date_creation");
                String filiere_interet = queryOutput.getString("filiere_interet");
                String motiv_etudiant = queryOutput.getString("motiv_etudiant");
                Integer status = queryOutput.getInt("statut");
                Integer ref_etudiant = queryOutput.getInt("ref_etudiant");

                // Stockage des noms et prénoms des étudiants dans des maps pour un acces rapide dans la recherche
                studentNames.put(ref_etudiant, queryOutput.getString("nom"));
                studentFirstNames.put(ref_etudiant, queryOutput.getString("prenom"));

                // Ajouter le dossier à la liste observable
                dossierObservableList.add(new Dossier(idDossier, date_creation, filiere_interet, motiv_etudiant, status, ref_etudiant));
            }

            // Configurer les colonnes de la TableView
            idStudentTableColumn.setCellValueFactory(new PropertyValueFactory<>("idDossier"));
            dateCreationTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
            statusTableColumn.setCellValueFactory(cellData -> {
                Integer status = cellData.getValue().getStatut();
                if (status == null) {
                    return new SimpleStringProperty("");
                } else if (status == 2) {
                    return new SimpleStringProperty("En attente");
                } else if (status == 0) {
                    return new SimpleStringProperty("Refusé");
                } else if (status == 1) {
                    return new SimpleStringProperty("Approuvé");
                } else {
                    return new SimpleStringProperty("");
                }
            });

            statusTableColumn.setCellFactory(col -> new TableCell<Dossier, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(item);
                    }
                }
            });

            DossierTableView.setItems(dossierObservableList);



            // Debut de la barre de recherche
            FilteredList<Dossier> filteredData = new FilteredList<>(dossierObservableList, b -> true);

            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(dossier -> {
                    // Si c'est vide on affiche TOUT
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    // Convertir la valeur de recherche en minuscule et enlever les espaces autour
                    String searchKeyword = newValue.trim().toLowerCase();

                    // Récupérer le nom et prénom de l'étudiant directement depuis les maps
                    String studentName = studentNames.get(dossier.getRefEtudiant()).toLowerCase();
                    String studentFirstName = studentFirstNames.get(dossier.getRefEtudiant()).toLowerCase();

                    // La regex (enlever les caractères spéciaux)
                    searchKeyword = searchKeyword.replaceAll("[^a-zA-Z0-9]", "");

                    return studentName.contains(searchKeyword) || studentFirstName.contains(searchKeyword);
                });
            });

            // Lier les données filtrées à la TableView
            DossierTableView.setItems(filteredData);

            // Configurer les colonnes pour afficher le nom et le prénom grace à la callback en parametre
            nameStudentTableColumn.setCellValueFactory(cellData -> {
                Dossier dossier = cellData.getValue();
                return new SimpleStringProperty(studentNames.get(dossier.getRefEtudiant()));
            });

            firstNameTableColumn.setCellValueFactory(cellData -> {
                Dossier dossier = cellData.getValue();
                return new SimpleStringProperty(studentFirstNames.get(dossier.getRefEtudiant()));
            });

            // Ajouter un événement de double-clic pour afficher les détails du dossier (GPT)
            DossierTableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Dossier selectedDossier = DossierTableView.getSelectionModel().getSelectedItem();
                    if (selectedDossier != null) {
                        DossierSingleton.getInstance().setDossierId(selectedDossier);
                        UserConnectedSingleton.getInstance();
                        try {
                            StudentSingleton.getInstance().setStudentId(StudentRepository.getById(selectedDossier));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/rdvView/rdv-view.fxml"));
                            Parent root = loader.load();
                            Stage currentStage = (Stage) DossierTableView.getScene().getWindow();
                            currentStage.setScene(new Scene(root));
                            currentStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
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