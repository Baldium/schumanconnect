package appli.schumanconnect.repository.SecretaryRepository;

import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.utils.Bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
