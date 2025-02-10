package appli.schumanconnect.controller.fournitureController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;

public class CreerFournitureController {

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
}