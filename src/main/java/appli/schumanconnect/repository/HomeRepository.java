package appli.schumanconnect.repository;

import appli.schumanconnect.controller.FlashMessage;
import appli.schumanconnect.utils.Bdd;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeRepository {

    public static int nbDossierTermine() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try{
            PreparedStatement reqNbDossierTermine = my_bdd.prepareStatement("SELECT COUNT(*) as nbDossierTermine FROM `dossiers` WHERE `statut` = ?");
            reqNbDossierTermine.setInt(1,1);
            ResultSet data = reqNbDossierTermine.executeQuery();
            if (data.next()) {
                int nbDossierTermine = data.getInt("nbDossierTermine");
                return nbDossierTermine;
            }
            else
                return 0;
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);

            throw new RuntimeException(e);
        }
    }

    public static int nbDossierAttente() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try{
            PreparedStatement reqNbDossierTermine = my_bdd.prepareStatement("SELECT COUNT(*) as nbDossierAttente FROM `dossiers` WHERE `statut` = ?");
            reqNbDossierTermine.setInt(1,2);
            ResultSet data = reqNbDossierTermine.executeQuery();
            if (data.next()) {
                int nbDossierTermine = data.getInt("nbDossierAttente");
                return nbDossierTermine;
            }
            else
                return 0;
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);

            throw new RuntimeException(e);
        }
    }

    public static int nbDossierRejete() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try{
            PreparedStatement reqNbDossierTermine = my_bdd.prepareStatement("SELECT COUNT(*) as nbDossierRejete FROM `dossiers` WHERE `statut` = ?");
            reqNbDossierTermine.setInt(1,0);
            ResultSet data = reqNbDossierTermine.executeQuery();
            if (data.next()) {
                int nbDossierTermine = data.getInt("nbDossierRejete");
                return nbDossierTermine;
            }
            else
                return 0;
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);

            throw new RuntimeException(e);
        }
    }

    public static double pourcentageDossierTermine() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try {
            PreparedStatement reqNbDossierTermine = my_bdd.prepareStatement(
                    "SELECT (COUNT(CASE WHEN statut = 1 THEN 1 END) * 100.0 / COUNT(*)) AS pourcentage_valide FROM `dossiers`");

            ResultSet data = reqNbDossierTermine.executeQuery();
            if (data.next()) {
                return data.getDouble("pourcentage_valide");
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }
    }

    public static double pourcentageDossierAttente() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try {
            PreparedStatement reqNbDossierAttente = my_bdd.prepareStatement(
                    "SELECT (COUNT(CASE WHEN statut = 2 THEN 1 END) * 100.0 / COUNT(*)) AS pourcentage_attente FROM `dossiers`");

            ResultSet data = reqNbDossierAttente.executeQuery();
            if (data.next()) {
                return data.getDouble("pourcentage_attente");
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }
    }

    public static double pourcentageDossierRejete() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try {
            PreparedStatement reqNbDossierRejete = my_bdd.prepareStatement(
                    "SELECT (COUNT(CASE WHEN statut = 0 THEN 1 END) * 100.0 / COUNT(*)) AS pourcentage_rejete FROM `dossiers`");

            ResultSet data = reqNbDossierRejete.executeQuery();
            if (data.next()) {
                return data.getDouble("pourcentage_rejete");
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }
    }
}
