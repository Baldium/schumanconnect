package appli.schumanconnect.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScenePage {
    public static void switchView(String fxmlPath, ActionEvent event) throws IOException {
        // Vérifier si l'événement provient d'un MenuItem
        Node source;

        if (event.getSource() instanceof Node) {
            source = (Node) event.getSource();
        } else {
            throw new IllegalArgumentException("L'événement source n'est ni un Node ni un MenuItem.");
        }

        // Charger la vue FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScenePage.class.getResource(fxmlPath)));

        // Obtenir la fenêtre actuelle et afficher la nouvelle scène
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
