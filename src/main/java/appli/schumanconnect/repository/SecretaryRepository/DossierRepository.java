package appli.schumanconnect.repository.SecretaryRepository;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;

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

}
