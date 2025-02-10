package appli.schumanconnect.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EmailService {
    private static final Map<String, Instant> tokenExpiryMap = new HashMap<>();
    private static final long TOKEN_VALIDITY_DURATION = 3600; // 1 heure en secondes

    public boolean sendPasswordResetEmail(String mailUser, String token) {
        try {
            Dotenv dotenv = Dotenv.load();
            tokenExpiryMap.put(token, Instant.now().plusSeconds(TOKEN_VALIDITY_DURATION));

            URI uri = new URI("https://api.mailjet.com/v3.1/send");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String apiKey = dotenv.get("apiKey");
            String secretKey = dotenv.get("secretKey");
            String credentials = apiKey + ":" + secretKey;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject emailData = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();

            message.put("From", new JSONObject().put("Email", "zelloufi.soulaimane@gmail.com").put("Name", "Admin"));
            message.put("To", new JSONArray().put(new JSONObject().put("Email", mailUser).put("Name", "User")));
            message.put("Subject", "Votre code de réinitialisation de mot de passe");
            message.put("TextPart", "Votre code de réinitialisation est : " + token + "\nCe code est valable pendant 1 heure.");

            messages.put(message);
            emailData.put("Messages", messages);

            OutputStream os = conn.getOutputStream();
            os.write(emailData.toString().getBytes());
            os.flush();

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(String mailUser, String sujet, int etat) {
        try {
            Dotenv dotenv = Dotenv.load();

            URI uri = new URI("https://api.mailjet.com/v3.1/send");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String apiKey = dotenv.get("apiKey");
            String secretKey = dotenv.get("secretKey");
            String credentials = apiKey + ":" + secretKey;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject emailData = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();

            message.put("From", new JSONObject().put("Email", "zelloufi.soulaimane@gmail.com").put("Name", "Admin"));
            message.put("To", new JSONArray().put(new JSONObject().put("Email", mailUser).put("Name", "User")));
            message.put("Subject", sujet);

            // Déterminer le contenu du message en fonction de l'état
            String emailBody = "";
            if (etat == 1) {
                emailBody = "Félicitations, votre dossier a été accepté !";
            } else if (etat == 0) {
                emailBody = "Malheureusement, votre dossier a été refusé.";
            } else if (etat == 2) {
                emailBody = "Votre dossier est en attente. Nous vous contacterons bientôt pour plus d'informations.";
            }

            message.put("TextPart", emailBody);

            messages.put(message);
            emailData.put("Messages", messages);

            OutputStream os = conn.getOutputStream();
            os.write(emailData.toString().getBytes());
            os.flush();

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isTokenValid(String token) {
        Instant expiryTime = tokenExpiryMap.get(token);
        return expiryTime != null && Instant.now().isBefore(expiryTime);
    }
}
