import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GitHubActivityFetcher {

    private static String GIT_HUB_URL = "https://api.github.com/users/";

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        if (args.length != 1) {
            System.out.println("Usage: java GitHubAcitvityFetcher <username> ");
            return;
        }

        String username = args[0];

        String apiURL = GIT_HUB_URL + username + "/events";

        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("GitHub user Activity:");
                parseAndDisplayActivity(response.toString());
            } else {
                System.out.println("Failed to fetch activity. HTTP Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("An error occured:" + e.getMessage());
        }

    }

    private static void parseAndDisplayActivity(String jsonResponse) {
        try {
            String[] events = jsonResponse.split("},");
            for (String event : events) {
                if (event.contains("\"type\":\"PushEvent\"")) {
                    System.out.println("_Pushed commits to a repository");
                } else if (event.contains("\"type\":\"IssuesEvent\"")) {
                    System.out.println("_Opened a new issue in a repository");
                } else if (event.contains("\"type\":\"WatchEvent\"")){
                    System.out.println("_Starred a repository");
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing activity data: " + e.getMessage());
        }
    }
}