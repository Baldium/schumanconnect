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

public class LoginController {
    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField passwordInput;


    UserConnectedSingleton UserConnected =  UserConnectedSingleton.getInstance();

    @FXML
    public void login(ActionEvent event) throws SQLException, IOException{
        User user = RegisterRepository.login(emailInput.getText(), passwordInput.getText());
        if(user != null)

        {
            UserConnected.setUserConnected(user);
            ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
        }
        System.out.println("mdp ou truc faux");
    }

    @FXML
    public void SwitchViewRegister(ActionEvent event) throws SQLException, IOException{
        ScenePage.switchView("/appli/schumanconnect/register-view.fxml", event);
    }


}
