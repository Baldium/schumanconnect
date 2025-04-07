package appli.schumanconnect.controller.fournitureController;

import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreerFournitureController {

    @FXML
    private VBox supplyContainer;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> supplierSelector;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addSupplyRow());
        saveButton.setOnAction(event -> saveSupplyRequest());
        loadSuppliers();
        configureSpinner();
    }

    private void configureSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantitySpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void addSupplyRow() {
        ComboBox<String> toolSelector = new ComboBox<>();
        toolSelector.getItems().addAll("Crayon", "Gomme", "Stylo");
        toolSelector.setValue("Crayon");

        Spinner<Integer> newQuantitySpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        newQuantitySpinner.setValueFactory(valueFactory);

        Label selectionLabel = new Label();
        toolSelector.setOnAction(event -> updateSelection(toolSelector, newQuantitySpinner, selectionLabel));
        newQuantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateSelection(toolSelector, newQuantitySpinner, selectionLabel));

        javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(10, toolSelector, newQuantitySpinner, selectionLabel);
        supplyContainer.getChildren().add(row);
    }

    private void updateSelection(ComboBox<String> toolSelector, Spinner<Integer> quantitySpinner, Label selectionLabel) {
        String tool = toolSelector.getValue();
        int quantity = quantitySpinner.getValue();
        selectionLabel.setText("Outil: " + tool + " | Quantité: " + quantity);
    }

    private void loadSuppliers() {
        List<String> suppliers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/schumanconnect", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nom FROM fournisseurs")) {

            while (resultSet.next()) {
                suppliers.add(resultSet.getString("nom"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        supplierSelector.getItems().addAll(suppliers);
        System.out.println("Fournisseurs chargés : " + suppliers); // Vérifiez les données chargées
    }

    @FXML
    private void saveSupplyRequest() {
        String fournisseur = supplierSelector.getValue();
        if (fournisseur == null || fournisseur.isEmpty()) {
            System.out.println("Aucun fournisseur sélectionné.");
            return;
        }

        for (javafx.scene.Node node : supplyContainer.getChildren()) {
            if (node instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox row = (javafx.scene.layout.HBox) node;
                ComboBox<String> toolSelector = (ComboBox<String>) row.getChildren().get(0);
                Spinner<Integer> quantitySpinner = (Spinner<Integer>) row.getChildren().get(1);
                String outil = toolSelector.getValue();
                int quantite = quantitySpinner.getValue();

                System.out.println("Enregistrement de la demande : Fournisseur = " + fournisseur + ", Outil = " + outil + ", Quantité = " + quantite);

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/schumanconnect", "root", "");
                     PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO demandes_fournitures (fournisseur, outil, quantite) VALUES (?, ?, ?)")) {
                    preparedStatement.setString(1, fournisseur);
                    preparedStatement.setString(2, outil);
                    preparedStatement.setInt(3, quantite);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Lignes affectées : " + rowsAffected);
                } catch (Exception e) {
                    System.out.println("Erreur lors de l'enregistrement de la demande : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Demandes de fournitures enregistrées.");
    }

    @FXML
    public void changePageSceneHome(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

    @FXML
    public void changePageSceneDossierInscription(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/dossierInscriptionView/dossierInscription-view.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }

    // Autres méthodes de changement de page...
}