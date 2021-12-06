package com.esiee.openclassroom;

import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiTools {

    static String user = "{\"username\": \"vinc\",\"password\": \"changeme\" }";
    // Make a GET Request
    public static String getJSONObjectFromURL(String urlString, String token) throws IOException, JSONException {
        HttpsURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.connect();

        String jsonString = "";

        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonString = sb.toString();
        }
        return jsonString;
    }

    // Make a POST Request
    public static String postJSONObjectToURL(String urlString) throws IOException, JSONException {
        HttpsURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream (
                urlConnection.getOutputStream());
        wr.writeBytes(user);
        wr.close();
        urlConnection.connect();

        String jsonString = "";

        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonString = sb.toString();
        }
        return jsonString;
    }

    public static User[] getAllUsers(String token){
        String baseUrl = BuildConfig.API_URL;
        String usersString = "";
        try {
            usersString = getJSONObjectFromURL(baseUrl + "users", token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        User[] users = null;
        try{
            users = mapper.readValue(usersString, User[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static String getToken(){
        String baseUrl = BuildConfig.API_URL;
        String token = "";
        try {
            String userToken = postJSONObjectToURL(baseUrl + "authentication_token");
            JSONObject tokenObject = new JSONObject(userToken);
            token = tokenObject.getString("token");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }


}
