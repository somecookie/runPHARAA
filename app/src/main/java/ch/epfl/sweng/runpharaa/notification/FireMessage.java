package ch.epfl.sweng.runpharaa.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FireMessage {
    //Server key found in firebase
    private final String SERVER_KEY = "AAAArozt-xs:APA91bEN2m2cVZVUlJ6GMRgacY42W7KF9f3Orpm1bcEKF0WbugfgDfwcKLZ1qmgs4d4EmD7iCgkB3qH6oXfjEk0vQEg2oodWioeYzve__QeCJr2PPgLI4qmHi71L5_EuxL87y1TJNlpX";
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private JSONObject root;

    public FireMessage(String title, String message) throws JSONException {
        root = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("message", message);
        root.put("data", data);
    }

    /**
     * Method that add the destination (key of the wanted device) to the message and send it
     */
    public String sendToToken(String token) throws Exception {
        root.put("to", token);
        return sendPushNotification();
    }


    /**
     * Method that creates the JSON file that will be send
     * @return SUCCESS if operation successful
     * @throws Exception
     */
    private String sendPushNotification() throws Exception {
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(root.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            System.out.println(builder);
            String result = builder.toString();

            JSONObject obj = new JSONObject(result);

            int success = Integer.parseInt(obj.getString("success"));
            if (success > 0) {
                return "SUCCESS";
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }
}