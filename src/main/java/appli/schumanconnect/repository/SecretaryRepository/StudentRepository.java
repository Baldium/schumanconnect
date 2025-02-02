package appli.schumanconnect.repository.SecretaryRepository;

import appli.schumanconnect.controller.FlashMessage;
import appli.schumanconnect.model.Dossier;
import appli.schumanconnect.model.Student;
import appli.schumanconnect.utils.Bdd;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

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
            reqInsertStudent.setString(4, student.getTel());
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


    public static void dropDossier(int idFicheEtudiante) throws SQLException {
        try {
            Connection my_bdd = Bdd.my_bdd();
            PreparedStatement reqVerifDossierExist = my_bdd.prepareStatement("SELECT e.*, d.* FROM etudiants AS e INNER JOIN dossiers AS d ON e.id_etudiant = d.ref_etudiant WHERE e.id_etudiant = ?");
            reqVerifDossierExist.setInt(1, idFicheEtudiante);
            ResultSet data = reqVerifDossierExist.executeQuery();
            boolean hasDossier = data.next();

            // Message de confirmation (GPT)
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet étudiant ?");

            if (hasDossier) {
                confirmation.setContentText("⚠️ Cet étudiant a un dossier associé !\n"
                        + "Supprimer cette fiche entraînera la perte du dossier lié.\n"
                        + "Cette action est irréversible.");
            } else {
                confirmation.setContentText("✅ Cette fiche étudiante n'a pas de dossier associé.\n"
                        + "Confirmez-vous la suppression ?");
            }

            // Attendre la réponse de l'utilisateur (GPT)
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        PreparedStatement reqDropStudent = my_bdd.prepareStatement("DELETE FROM etudiants WHERE id_etudiant = ?");
                        reqDropStudent.setInt(1, idFicheEtudiante);
                        reqDropStudent.executeUpdate();

                        // Message de succès
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Suppression réussie");
                        success.setHeaderText(null);
                        success.setContentText("L'étudiant a été supprimé avec succès.");
                        success.showAndWait();

                    } catch (SQLException e) {
                        throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            throw new RuntimeException("Erreur : " + e.getMessage());
        }
    }

    public static void updateStudent(int idFicheEtudiante, String email, String telephone, String adresse, String diplome) throws SQLException {

        Connection my_bdd = Bdd.my_bdd();
        PreparedStatement checkMail = my_bdd.prepareStatement("SELECT COUNT(*) FROM etudiants WHERE email = ?");
        checkMail.setString(1, email);
        ResultSet data = checkMail.executeQuery();
        if (data.next() && data.getInt(1) > 0) {
            FlashMessage.show("L'email existe déjà dans la base.", Alert.AlertType.WARNING);
            return;
        }
        String updateQuery = "UPDATE etudiants SET email = ?, tel = ?, adresse = ?, dernier_diplome_obtenu = ? WHERE id_etudiant = ?";

        PreparedStatement preparedStatement = my_bdd.prepareStatement(updateQuery);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, telephone);
        preparedStatement.setString(3, adresse);
        preparedStatement.setString(4, diplome);
        preparedStatement.setInt(5, idFicheEtudiante);

        preparedStatement.executeUpdate();
    }

    public static Student getById(Dossier dossier) throws SQLException {
        try {
        Connection my_bdd = Bdd.my_bdd();
        PreparedStatement reqStudentById = my_bdd.prepareStatement("SELECT e.* FROM etudiants e INNER JOIN dossiers d ON e.id_etudiant = d.ref_etudiant WHERE d.id_dossier = ?");
        reqStudentById.setInt(1, dossier.getIdDossier());
        ResultSet data = reqStudentById.executeQuery();
        if (data.next())
            return new Student(data.getInt("id_etudiant"), data.getString("nom"), data.getString("prenom"), data.getString("email"), data.getString("tel"), data.getString("adresse"), data.getString("dernier_diplome_obtenu"));



        return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}

