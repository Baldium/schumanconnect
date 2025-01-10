package appli.schumanconnect.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import appli.schumanconnect.utils.*;

public class HomeController {
    @FXML
    private void handleHelloButtonClick(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/homeView/homePage-view.fxml", event);
    }

}
