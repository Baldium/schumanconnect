package appli.schumanconnect.controller;

import appli.schumanconnect.service.ServiceFactory;
import appli.schumanconnect.utils.ScenePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField emailInput;

    @FXML
    public void sendMail(ActionEvent event) throws IOException {
        String email = emailInput.getText();
        String token = ServiceFactory.getPasswordResetService().generateToken();

        if (ServiceFactory.getPasswordResetService().storeToken(email, token)) {
            ServiceFactory.getEmailService().sendPasswordResetEmail(email, token);

            // Afficher un message de succès avec une alerte
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText("E-mail envoyé !");
            successAlert.setContentText("Un e-mail de réinitialisation de mot de passe a été envoyé à l'adresse : " + email);
            successAlert.showAndWait();

            ScenePage.switchView("/appli/schumanconnect/resetPassword.fxml", event);

        } else {
            // Afficher une alerte d'erreur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Adresse non reconnue");
            errorAlert.setContentText("Aucun compte n'est associé à cette adresse e-mail.");
            errorAlert.showAndWait();

            ScenePage.switchView("/appli/schumanconnect/register-view.fxml", event);
        }
    }

}
