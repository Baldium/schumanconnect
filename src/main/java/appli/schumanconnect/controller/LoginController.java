package appli.schumanconnect.controller;

import appli.schumanconnect.*;
import appli.schumanconnect.model.User;
import appli.schumanconnect.repository.RegisterRepository;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label flashMessage;

    UserConnectedSingleton UserConnected = UserConnectedSingleton.getInstance();

    @FXML
    public void login(ActionEvent event) throws SQLException, IOException {
        try {
            User user = RegisterRepository.login(emailInput.getText(), passwordInput.getText());
            if (user != null) {
                System.out.println("Connexion réussie !");
                UserConnected.setUserConnected(user);
                ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
            } else {
                throw new FlashMessage("Identifiants incorrects !");
            }
        } catch (FlashMessage e) {
            System.out.println(e.getMessage());
            showFlashMessage(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showFlashMessage("Une erreur inattendue est survenue.");
        }
    }

    @FXML
    public void SwitchViewRegister(ActionEvent event) throws SQLException, IOException {
        ScenePage.switchView("/appli/schumanconnect/register-view.fxml", event);
    }

    // GPT
    private void showFlashMessage(String message) {
        Platform.runLater(() -> {
            flashMessage.setText(message);

            // Planifie l'effacement du message après 3 secondes
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), evt -> flashMessage.setText(""))
            );
            timeline.setCycleCount(1); // Exécute une seule fois
            timeline.play();
        });
    }

    @FXML
    public void ChangeScenePageForgotPassword(ActionEvent event) throws IOException{
        ScenePage.switchView("/appli/schumanconnect/forgotPassword-view.fxml", event);
    }
}
