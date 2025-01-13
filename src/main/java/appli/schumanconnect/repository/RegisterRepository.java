package appli.schumanconnect.repository;

import javafx.event.ActionEvent;
import appli.schumanconnect.model.*;
import appli.schumanconnect.utils.*;
import appli.schumanconnect.repository.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterRepository {
    UserConnectedSingleton UserConnected =  UserConnectedSingleton.getInstance();


    public static User login(String email, String password) throws SQLException {
        Connection conn = Bdd.my_bdd();
        PreparedStatement req = conn.prepareStatement(
                "SELECT * FROM users WHERE email = ?"
        );
        req.setString(1, email);
        ResultSet sqlUser = req.executeQuery();
        if (sqlUser.next()) {
            String hashedPassword = sqlUser.getString("mdp");

            if (BCrypt.checkpw(password, hashedPassword))
            {
                String roleStr = sqlUser.getString("role"); // Assure-toi que c'est bien un String dans la DB
                Role role = Role.valueOf(roleStr.toUpperCase());// Convertir la chaîne en Role enum
                System.out.println("Connexion parfait");
                User user = new User(sqlUser.getInt("id_user"),sqlUser.getString("nom"), sqlUser.getString("prenom"), sqlUser.getString("email"), sqlUser.getString("mdp"), role, sqlUser.getString("derniere_connexion"));
                return user;
            } else
            {
                System.out.println("Mdp incorrect");
                return null;
            }
        } else
        {
            return null;
        }
    }

    public static int register(String email, String mdp, String nom, String prenom, String confirmPassword, String role) throws SQLException, IOException {
        if (!mdp.equals(confirmPassword)) {
            System.out.println("Les mots de passe ne correspondent pas !");
            return 0;
        }

        Connection conn = Bdd.my_bdd();

        PreparedStatement checkEmail = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
        checkEmail.setString(1, email);
        ResultSet rs = checkEmail.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Cet email est déjà utilisé !");
            return 0;
        }

        String hashedPassword = BCrypt.hashpw(mdp, BCrypt.gensalt());

        PreparedStatement reqInsert = conn.prepareStatement(
                "INSERT INTO users (email, mdp, nom, prenom, role, derniere_connexion) VALUES (?, ?, ?, ?, ?, ?)"
        );

        // Rôle : on récupère le rôle sous forme de chaîne et on le convertit en enum Role
        Role userRole = Role.valueOf(role.toUpperCase()); // Assurer que le rôle est en majuscule


        reqInsert.setString(1, email);
        reqInsert.setString(2, hashedPassword);
        reqInsert.setString(3, nom);
        reqInsert.setString(4, prenom);
        reqInsert.setString(5, userRole.name()); // Sauvegarde le rôle sous forme de String
        reqInsert.setString(6, null);

        int rowsAffected = reqInsert.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Inscription réussie !");
            return 1;
        } else {
            System.out.println("Une erreur est survenue pendant l'inscription.");
            return 0;
        }
    }
}
