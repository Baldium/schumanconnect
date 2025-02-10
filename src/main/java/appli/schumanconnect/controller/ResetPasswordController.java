package appli.schumanconnect.controller;

import appli.schumanconnect.service.ServiceFactory;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.ScenePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetPasswordController {

    @FXML
    private TextField tokenField;
    @FXML
    private PasswordField newPasswordField;

    @FXML
    public void resetPassword(ActionEvent event) throws IOException {
        String token = tokenField.getText();
        String newPassword = newPasswordField.getText();

        if (ServiceFactory.getPasswordResetService().isTokenValid(token)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            try (Connection my_bdd = Bdd.my_bdd()) {
                PreparedStatement stmt = my_bdd.prepareStatement("UPDATE users SET mdp = ?, reset_token = NULL WHERE reset_token = ?");
                stmt.setString(1, hashedPassword);
                stmt.setString(2, token);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Affichage de l'alerte de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText("Mot de passe mis à jour !");
                    successAlert.setContentText("Votre mot de passe a été mis à jour avec succès.");
                    successAlert.showAndWait();
                    ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);

                } else {
                    // Si aucune ligne n'a été mise à jour
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText("Échec de la mise à jour");
                    errorAlert.setContentText("Il y a eu un problème lors de la mise à jour de votre mot de passe.");
                    errorAlert.showAndWait();
                    ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
                }
                ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);

            } catch (SQLException e) {
                e.printStackTrace();
                // Affichage de l'alerte d'erreur en cas d'exception SQL
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur de connexion à la base de données");
                errorAlert.setContentText("Une erreur est survenue lors de la mise à jour du mot de passe. Veuillez réessayer plus tard.");
                errorAlert.showAndWait();
                ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Alerte si le token est invalide ou expiré
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Token invalide ou expiré");
            errorAlert.setContentText("Le token que vous avez utilisé est invalide ou a expiré. Veuillez réessayer.");
            errorAlert.showAndWait();
            ScenePage.switchView("/appli/schumanconnect/resetPassword.fxml", event);
        }
    }
}
