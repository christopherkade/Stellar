package stellar.kade_c.com;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contain the API request methods.
 */
public class APIRequests {

    final String api_key = "iKfNjmcc1nUTfijTVsx99Tt76yQGWYLJW4ZJdZDb";

    /**
     * Astronomy Picture of the Day API Request
     */
    public JSONObject getAPD(String date) {
        String urlStr;
        URL url;

        try {
            urlStr = "https://api.nasa.gov/planetary/apod?api_key=" + api_key;

            urlStr += "&date=" + date;

            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Request not successful
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(jsonString.toString());

            return jsonObject;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
