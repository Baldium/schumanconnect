package appli.schumanconnect.controller.fournitureController;

import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DemandeFournitureController {

    @FXML
    private VBox supplyContainer; // Conteneur pour les fournitures

    @FXML
    private Button addButton; // Bouton pour ajouter une fourniture

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addSupplyRow());
    }

    private void addSupplyRow() {
        // Créer un ComboBox pour les outils
        ComboBox<String> toolSelector = new ComboBox<>();
        toolSelector.getItems().addAll("Crayon", "Gomme", "Pinceau");
        toolSelector.setValue("Crayon"); // Valeur par défaut

        // Créer un Spinner pour la quantité
        Spinner<Integer> quantitySpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantitySpinner.setValueFactory(valueFactory);

        // Créer un label pour afficher la sélection
        Label selectionLabel = new Label();
        toolSelector.setOnAction(event -> updateSelection(toolSelector, quantitySpinner, selectionLabel));
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateSelection(toolSelector, quantitySpinner, selectionLabel));

        // Ajouter les éléments à une ligne horizontale (HBox)
        javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(10, toolSelector, quantitySpinner, selectionLabel);
        supplyContainer.getChildren().add(row);
    }

    private void updateSelection(ComboBox<String> toolSelector, Spinner<Integer> quantitySpinner, Label selectionLabel) {
        String tool = toolSelector.getValue();
        int quantity = quantitySpinner.getValue();
        selectionLabel.setText("Outil: " + tool + " | Quantité: " + quantity);
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



}