package appli.schumanconnect.controller;

import appli.schumanconnect.*;
import appli.schumanconnect.model.User;
import appli.schumanconnect.repository.RegisterRepository;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField nomInput;
    @FXML
    private TextField prenomInput;

    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField mdpInput;

    @FXML
    private PasswordField mdpInputConfirm;

    @FXML
    private TextField roleInput;

    UserConnectedSingleton UserConnected =  UserConnectedSingleton.getInstance();


    public void register(ActionEvent event) throws SQLException, IOException {
        int succes_register = RegisterRepository.register(emailInput.getText(),mdpInput.getText(),nomInput.getText(), prenomInput.getText(),
                mdpInputConfirm.getText(), roleInput.getText());
        User user = RegisterRepository.login(emailInput.getText(), mdpInput.getText());
        UserConnected.setUserConnected(user);
        if (succes_register == 0)
        {
            ScenePage scene = new ScenePage();
            scene.switchView("/appli/schumanconnect/register-view.fxml", event);
        }
        else if (succes_register == 1)
        {
            ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
        }
        else
            System.out.println("pb");
    }

    public void SwitchViewLogin(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }

}
