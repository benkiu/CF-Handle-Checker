import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        while (true) {
            if (checkStatus(getUserInput())) {
                System.out.println("Handle Already Used!");
            } else {
                System.out.println("Handle Is Available");
            }
        }
    }
    public static String getUserInput() {
        Scanner in = new Scanner(System.in);
        System.out.print("CF Handle : ");
        return in.next();
    }
    public static HttpURLConnection initConnection (String handle) throws IOException {
        URL url = new URL("https://codeforces.com/api/user.info?handles=" + handle);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }
    public static String getResponse(String handle) throws IOException {
        HttpURLConnection conn = initConnection(handle);
        if (conn.getResponseCode() == 400) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseString = new StringBuilder();
        int cp;
        while ((cp = in.read()) != -1) {
            responseString.append((char) cp);
        }
        return responseString.toString();
    }
    public static JSONObject getJSONResponse(String handle) throws IOException {
        String response = getResponse(handle);
        if (response == null) {
            return null;
        }
        return new JSONObject(response);
    }
    public static String getStatus(String handle) throws IOException {
        JSONObject response = getJSONResponse(handle);
        if (response == null) {
            return "FAILED";
        }
        return response.getString("status");
    }
    public static boolean checkStatus(String handle) throws IOException {
        return getStatus(handle).equals("OK");
    }
}