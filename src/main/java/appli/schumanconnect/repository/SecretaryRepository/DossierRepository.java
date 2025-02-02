package appli.schumanconnect.repository.SecretaryRepository;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DossierRepository {
    public static void addDossierStudent(Dossier dossierStudent) throws SQLException {
        Connection my_bdd = Bdd.my_bdd();
        PreparedStatement reqInsertDossier = my_bdd.prepareStatement(
                "INSERT INTO `dossiers` (`date_creation`, `filiere_interet`, `motiv_etudiant`, `statut`, `ref_etudiant`) VALUES (?, ?, ?, ?, ?)");
        reqInsertDossier.setString(1, dossierStudent.getDateCreation());
        reqInsertDossier.setString(2, dossierStudent.getFiliereInteret());
        reqInsertDossier.setString(3, dossierStudent.getMotivEtudiant());
        reqInsertDossier.setInt(4, dossierStudent.getStatut());
        reqInsertDossier.setInt(5, dossierStudent.getRefEtudiant());

        reqInsertDossier.executeUpdate();
    }

    public static boolean dossierExists(Dossier dossier) throws SQLException {
        Connection my_bdd = Bdd.my_bdd();
        PreparedStatement reqVerifDossierExist = my_bdd.prepareStatement("SELECT COUNT(*) FROM dossiers WHERE ref_etudiant = ?");
        reqVerifDossierExist.setInt(1, dossier.getRefEtudiant());
        ResultSet data = reqVerifDossierExist.executeQuery();
            if (data.next() && data.getInt(1) > 0)
                return true;
        return false;
    }

    public static ResultSet getDossierStudent(Dossier dossier) throws SQLException{
        Connection my_bdd = Bdd.my_bdd();
        PreparedStatement reqGetDossierStudent = my_bdd.prepareStatement("SELECT d.*, e.* FROM `dossiers` as d INNER JOIN etudiants as e WHERE d.id_dossier = ? AND e.id_etudiant = d.ref_etudiant");
        reqGetDossierStudent.setInt(1, dossier.getIdDossier());
        ResultSet data = reqGetDossierStudent.executeQuery();
        return data;
    }

    public static void dropDossier(Dossier dossier) throws SQLException{

        try {
            Connection my_bdd = Bdd.my_bdd();
            PreparedStatement reqDropDossierStudent = my_bdd.prepareStatement("DELETE FROM `dossiers` WHERE id_dossier = ?");
            reqDropDossierStudent.setInt(1, dossier.getIdDossier());

            PreparedStatement reqDropFicheStudent = my_bdd.prepareStatement("DELETE FROM `etudiants` WHERE id_etudiant = ?");
            reqDropFicheStudent.setInt(1, dossier.getRefEtudiant());

            // Message de confirmation (GPT)
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer ce dossier ?");

            // Attendre la réponse de l'utilisateur (GPT)
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        reqDropDossierStudent.executeUpdate();
                        reqDropFicheStudent.executeUpdate();

                        // Message de succès
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Suppression réussie");
                        success.setHeaderText(null);
                        success.setContentText("Le dossier a été supprimé avec succès.");
                        success.showAndWait();

                    } catch (SQLException e) {
                        throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
        throw new RuntimeException("Erreur : " + e.getMessage());}
    }

    public static void updateDossier(int idDossier, String filiere, String motivation, String statut) throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        String updateQuery = "UPDATE dossiers SET filiere_interet = ?, motiv_etudiant = ?, statut = ? WHERE id_dossier = ?";
        PreparedStatement preparedStatement = my_bdd.prepareStatement(updateQuery);

        preparedStatement.setString(1, filiere);
        preparedStatement.setString(2, motivation);
        preparedStatement.setString(3, statut);
        preparedStatement.setInt(4, idDossier);

        preparedStatement.executeUpdate();
    }

}
