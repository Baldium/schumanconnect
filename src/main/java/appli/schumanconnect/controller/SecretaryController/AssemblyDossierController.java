package appli.schumanconnect.controller.SecretaryController;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.DossierSingleton;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.StudentSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AssemblyDossierController implements Initializable {

    @FXML
    private Label StudentIdLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Student StudentId = StudentSingleton.getInstance().getStudentId();

        StudentIdLabel.setText("Etudiant ID: " + StudentId.getIdEtudiant());

        loadDossierDetails(StudentId.getIdEtudiant());
    }

    private void loadDossierDetails(int dossierId) {

    }

}

