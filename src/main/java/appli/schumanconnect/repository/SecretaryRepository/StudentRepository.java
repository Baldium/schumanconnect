package appli.schumanconnect.repository.SecretaryRepository;

import appli.schumanconnect.controller.FlashMessage;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.utils.Bdd;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRepository {
    public static int register(Student student) throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try {
            // Vérifier si l'email existe déjà dans la base
            PreparedStatement checkMail = my_bdd.prepareStatement("SELECT COUNT(*) FROM etudiants WHERE email = ?");
            checkMail.setString(1, student.getEmail());
            ResultSet data = checkMail.executeQuery();
            if (data.next() && data.getInt(1) > 0) {
                FlashMessage.show("L'email existe déjà dans la base.", Alert.AlertType.WARNING);
                return 0;
            }

            PreparedStatement reqInsertStudent = my_bdd.prepareStatement(
                    "INSERT INTO `etudiants`(`nom`, `prenom`, `email`, `tel`, `adresse`, `dernier_diplome_obtenu`) VALUES (?, ?, ?, ?, ?, ?)"
            );

            reqInsertStudent.setString(1, student.getNom());
            reqInsertStudent.setString(2, student.getPrenom());
            reqInsertStudent.setString(3, student.getEmail());
            reqInsertStudent.setInt(4, student.getTel());
            reqInsertStudent.setString(5, student.getAdresse());
            reqInsertStudent.setString(6, student.getDernierDiplomeObtenu());

            int succes = reqInsertStudent.executeUpdate();

            if (succes > 0) {
                FlashMessage.show("Étudiant inscrit avec succès.", Alert.AlertType.INFORMATION);
                return 1;
            } else {
                FlashMessage.show("Une erreur est survenue pendant l'inscription.", Alert.AlertType.ERROR);
                return 0;
            }

        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }
    }
}
