package appli.schumanconnect.controller.SecretaryController;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import appli.schumanconnect.model.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CreateDossierController implements Initializable {

    @FXML
    private TableView<Student> StudentTableView;

    @FXML
    private TableColumn<Student, String> nomTableColumn;

    @FXML
    private TableColumn<Student, String> prenomTableColumn;

    @FXML
    private TableColumn<Student, String> mailTableColumn;

    @FXML
    private TableColumn<Integer, String> telTableColumn;

    @FXML
    private TableColumn<Student, String> lastDiplomeTableColumn;

    @FXML
    private TextField keywordTextField;

    ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    // Du coup ici mes maps ne servent à rien j'utilise directement lobjet student (les fonctione) getter pour ma barre de recherche !
    private  Map<Integer, String> studentNom = new HashMap<>();
    private Map<Integer, String> studentPrenom = new HashMap<>();
    private Map<Integer, String> studentMail = new HashMap<>();
    private Map<Integer, String> studentLastDiplome = new HashMap<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection my_bdd = null;
        try {
            my_bdd = Bdd.my_bdd();
            PreparedStatement reqGetAllStudents = my_bdd.prepareStatement("SELECT * FROM `etudiants`");
            ResultSet data = reqGetAllStudents.executeQuery();

            while (data.next())
            {
                Integer id_student = data.getInt("id_etudiant");
                String nom = data.getString("nom");
                String prenom = data.getString("prenom");
                String mail = data.getString("email");
                Integer telephone = data.getInt("tel");
                String adresse = data.getString("adresse");
                String lastDiplome = data.getString("dernier_diplome_obtenu");


                studentNom.put(id_student, nom);
                studentPrenom.put(id_student, prenom);
                studentMail.put(id_student, mail);
                studentLastDiplome.put(id_student, lastDiplome);

                studentObservableList.add(new Student(id_student, nom, prenom, mail, telephone, adresse, lastDiplome));
            }

            nomTableColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomTableColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            mailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            telTableColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
            lastDiplomeTableColumn.setCellValueFactory(new PropertyValueFactory<>("dernierDiplomeObtenu"));
            StudentTableView.setItems(studentObservableList);

            // Barre de recherche
            FilteredList<Student> filteredData = new FilteredList<>(studentObservableList, b -> true);

            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(student -> {
                    // Si c'est vide on affiche TOUT
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    // Convertir la valeur de recherche en minuscule et enlever les espaces autour
                    String searchKeyword = newValue.trim().toLowerCase();

                    String studentName = student.getNom().toLowerCase();
                    String studentFirstName = student.getPrenom().toLowerCase();
                    String studentEmail = student.getEmail().toLowerCase();
                    String studentDernierDiplome = student.getDernierDiplomeObtenu().toLowerCase();


                    // La regex (enlever les caractères spéciaux)
                    searchKeyword = searchKeyword.replaceAll("[^a-zA-Z0-9@.\\-+]", "");

                    return studentName.contains(searchKeyword) || studentFirstName.contains(searchKeyword) || studentEmail.contains(searchKeyword) || studentDernierDiplome.contains(searchKeyword);
                });
            });

            StudentTableView.setItems(filteredData);

            // Ajouter un événement de double-clic pour afficher les détails du dossier (GPT)
            StudentTableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Student selectedDossier = StudentTableView.getSelectionModel().getSelectedItem();
                    if (selectedDossier != null) {
                        StudentSingleton.getInstance().setStudentId(selectedDossier);
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/appli/schumanconnect/secretaryView/AssemblyDossier-view.fxml"));
                            Parent root = loader.load();
                            Stage currentStage = (Stage) StudentTableView.getScene().getWindow();
                            currentStage.setScene(new Scene(root));
                            currentStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });



        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
