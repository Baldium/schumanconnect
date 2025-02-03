package appli.schumanconnect.service;

import appli.schumanconnect.utils.Bdd;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class PasswordResetService {

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean storeToken(String email, String token) {
        try (Connection my_bdd = Bdd.my_bdd()) {
            PreparedStatement stmt = my_bdd.prepareStatement("UPDATE users SET reset_token = ?, token_expiry = DATE_ADD(NOW(), INTERVAL 1 HOUR) WHERE email = ?");
            stmt.setString(1, token);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try (Connection my_bdd = Bdd.my_bdd()) {
            PreparedStatement stmt = my_bdd.prepareStatement(
                    "SELECT id_user FROM users WHERE reset_token = ? AND token_expiry > NOW()");
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
