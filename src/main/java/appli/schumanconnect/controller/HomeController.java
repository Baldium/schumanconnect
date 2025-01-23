package appli.schumanconnect.controller;

import appli.schumanconnect.model.User;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label labelName;


    UserConnectedSingleton UserConnected = UserConnectedSingleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = UserConnected.getUserConnected();
        if (user != null)
            labelName.setText("Bienvenue " + user.getPrenom() + " " + user.getNom() + " !");
        else
            labelName.setText("Bienvenue !");

    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        UserConnectedSingleton.getInstance().logout();
        ScenePage.switchView("/appli/schumanconnect/login-view.fxml", event);
    }

    @FXML
    public void changePageToAllStudent(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml", event);
    }


}
