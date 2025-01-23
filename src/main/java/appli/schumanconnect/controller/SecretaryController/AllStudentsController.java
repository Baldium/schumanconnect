package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.DossierSingleton;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AllStudentsController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Ici, c'est la requête SQL que la majorité des gens feront, puis ils feront deux constructeurs dans le modèle Dossier : un pour afficher le nom, le prénom et le reste des informations, et un autre constructeur pour créer/modifier un dossier.
            String req = "SELECT d.*, e.* FROM dossiers as d INNER JOIN etudiants as e on d.ref_etudiant = e.id_etudiant";
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

                dossierObservableList.add(new Dossier(idDossier, date_creation, filiere_interet, motiv_etudiant, status, ref_etudiant));
            }

            idStudentTableColumn.setCellValueFactory(new PropertyValueFactory<>("idDossier"));
            dateCreationTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
            // Configurer la colonne statut
            statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

            // Configurer la colonne statut avec un type String pour les valeurs de texte (GPT)
            statusTableColumn.setCellValueFactory(cellData -> {
                Integer status = cellData.getValue().getStatut();
                if (status == null) {
                    return new SimpleStringProperty("");  // Si le statut est null
                } else if (status == 2) {
                    return new SimpleStringProperty("En attente");
                } else if (status == 0) {
                    return new SimpleStringProperty("Refusé");
                } else if (status == 1) {
                    return new SimpleStringProperty("Approuvé");
                } else {
                    return new SimpleStringProperty(""); // Si une autre valeur
                }
            });

            // Personnaliser l'affichage de la colonne statut (GPT)
            statusTableColumn.setCellFactory(col -> new TableCell<Dossier, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("");  // Si l'élément est vide ou null
                    } else {
                        setText(item); // Afficher le texte correspondant au statut
                    }
                }
            });

            // Ajouter les données à la TableView
            DossierTableView.setItems(dossierObservableList);

            // Barre de recherche
            FilteredList<Dossier> filteredData = new FilteredList<>(dossierObservableList, b -> true);

            // Écouter les changements dans la barre de recherche
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(dossier -> {
                    // Si la recherche est vide, on affiche tout
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    // Convertir la valeur de recherche en minuscule et enlever les espaces autour
                    String searchKeyword = newValue.trim().toLowerCase();

                    // Récupérer le nom et le prénom de l'étudiant lié à ce dossier
                    String studentName = getStudentNameById(dossier.getRefEtudiant()).toLowerCase();

                    // Appliquer un filtrage plus robuste (suppression de caractères spéciaux comme les virgules)
                    searchKeyword = searchKeyword.replaceAll("[^a-zA-Z0-9]", "");  // Supprime les caractères non alphabétiques

                    // Si le nom ou le prénom contient la valeur de recherche, on garde le dossier dans les résultats filtrés
                    return studentName.replaceAll("[^a-zA-Z0-9]", "").contains(searchKeyword);
                });
            });

            // Lier les données filtrées à la TableView
            DossierTableView.setItems(filteredData);


            // Je mets la Callback dans les paramètres
            nameStudentTableColumn.setCellValueFactory(cellData -> {
                Dossier dossier = cellData.getValue();
                return new SimpleStringProperty(getStudentNameById(dossier.getRefEtudiant()));
            });

            firstNameTableColumn.setCellValueFactory(cellData -> {
                Dossier dossier = cellData.getValue();
                return new SimpleStringProperty(getStudentFirstNameById(dossier.getRefEtudiant()));
            });

            // Ajouter l'événement de double-clic sur une ligne de la TableView (GPT)
            DossierTableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {  // Vérifie si le clic est un double-clic
                    Dossier selectedDossier = DossierTableView.getSelectionModel().getSelectedItem();
                    if (selectedDossier != null) {
                        DossierSingleton.getInstance().setDossierId(selectedDossier);
                        try {
                            // Charger le fichier FXML de la nouvelle vue
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/viewDossier-view.fxml"));
                            Parent root = loader.load();

                            // Obtenir le Stage (fenêtre principale) actuel
                            Stage currentStage = (Stage) DossierTableView.getScene().getWindow();

                            // Changer la scène
                            currentStage.setScene(new Scene(root));
                            currentStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour récupérer le nom dans la callback
    public String getStudentNameById(int refEtudiant) {
        String name = "";
        try {
            String query = "SELECT nom FROM etudiants WHERE id_etudiant = ?";
            Connection conn = Bdd.my_bdd();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, refEtudiant);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getStudentFirstNameById(int refEtudiant) {
        String firstName = "";
        try {
            String query = "SELECT prenom FROM etudiants WHERE id_etudiant = ?";
            Connection conn = Bdd.my_bdd();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, refEtudiant);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                firstName = resultSet.getString("prenom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstName;
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }
}
