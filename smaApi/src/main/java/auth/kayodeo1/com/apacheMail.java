package auth.kayodeo1.com;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class apacheMail {
    private static final String API_KEY = "YOUR_MAILGUN_API_KEY";
    private static final String DOMAIN = "YOUR_MAILGUN_DOMAIN";

    public static void main(String[] args) {
        try {
            URL url = new URL("https://api.mailgun.net/v3/" + DOMAIN + "/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(("api:" + API_KEY).getBytes()));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "from=you@yourdomain.com&to=recipient@example.com&subject=Test Email&text=Hello from Mailgun!";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response (not shown for brevity)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
