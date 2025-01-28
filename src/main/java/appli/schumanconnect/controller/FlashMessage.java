package appli.schumanconnect.controller;

import javafx.scene.control.Alert;

public class FlashMessage extends RuntimeException {
    public FlashMessage(String message) {
        super(message);
    }

    public static void show(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(message);
        alert.showAndWait();
    }
}