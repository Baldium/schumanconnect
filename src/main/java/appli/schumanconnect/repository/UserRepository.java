package appli.schumanconnect.repository;

import appli.schumanconnect.controller.FlashMessage;
import appli.schumanconnect.model.Role;
import appli.schumanconnect.model.User;
import appli.schumanconnect.utils.Bdd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    public static ObservableList<User> getAllUser() throws SQLException {
        Connection my_bdd = Bdd.my_bdd();

        try {
            ObservableList<User> users = FXCollections.observableArrayList();
            Role role;
            PreparedStatement requete = my_bdd.prepareStatement("SELECT * FROM `users`");
            ResultSet recup = requete.executeQuery();


            while (recup.next()) {

                String roleString = recup.getString("role");
                if (Objects.equals(roleString, "PROFESSEUR")) {
                    role = Role.PROFESSEUR;
                } else if (Objects.equals(roleString, "SECRETAIRE")) {
                    role = Role.SECRETAIRE;
                } else if (Objects.equals(roleString, "GESTIONNAIRE")) {
                    role = Role.GESTIONNAIRE;
                } else if (Objects.equals(roleString, "ADMIN")) {
                    role = Role.ADMIN;
                }
                else
                {
                    throw new Exception();
                }

                users.add(new User(recup.getInt("id_user"), recup.getString("nom"), recup.getString("prenom"), recup.getString("email"), recup.getString("mdp"), role, recup.getString("derniere_connexion")));
            }
            return users;

        } catch (Exception e) {
            FlashMessage.show("Erreur : " + e.getMessage(), Alert.AlertType.ERROR);

            throw new RuntimeException(e);
        }
    }

}
