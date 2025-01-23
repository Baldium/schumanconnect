package appli.schumanconnect.controller.SecretaryController;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;
import appli.schumanconnect.utils.DossierSingleton;
import appli.schumanconnect.utils.ScenePage;
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

public class ViewDossierController implements Initializable {

    @FXML
    private Label dossierIdLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Dossier dossierId = DossierSingleton.getInstance().getDossierId();

        dossierIdLabel.setText("Dossier ID: " + dossierId.getIdDossier());

        loadDossierDetails(dossierId.getIdDossier());
    }

    private void loadDossierDetails(int dossierId) {
        String query = "SELECT * FROM dossiers WHERE id_dossier = ?";
        try (Connection conn = Bdd.my_bdd();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, dossierId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String dateCreation = rs.getString("date_creation");
                String filiereInteret = rs.getString("filiere_interet");
                String motivEtudiant = rs.getString("motiv_etudiant");
                int statut = rs.getInt("statut");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onBackButtonClicked(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/secretaryView/allStudents.fxml", event);
    }
}

