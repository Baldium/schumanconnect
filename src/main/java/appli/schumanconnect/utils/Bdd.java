package appli.schumanconnect.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class Bdd {
    public static Connection my_bdd() throws SQLException {
        Dotenv dotenv = Dotenv.load();
        String dbUrl = dotenv.get("DATABASE_URL");
        String dbUser = dotenv.get("DATABASE_USER");
        String dbPassword = dotenv.get("DATABASE_PASSWORD");
        Connection my_bdd = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
        return my_bdd;
    }
}