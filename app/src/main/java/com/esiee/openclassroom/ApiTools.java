package com.esiee.openclassroom;

import com.esiee.openclassroom.model.Score;
import com.esiee.openclassroom.model.User;
import com.esiee.openclassroom.model.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class ApiTools {

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
    public static String postJSONObjectToURL(String urlString, String token, String body) throws IOException, JSONException {
        System.out.println(body);
        HttpsURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Accept","application/json");
        if(token != null){
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        }
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream (
                urlConnection.getOutputStream());
        wr.writeBytes(body);
        wr.close();
        urlConnection.connect();

        String jsonString = "";

        //On vérifie le code comme ça pour prendre en compte les codes 201, etc.
        System.out.println(urlConnection.getResponseCode());
        if (("" + urlConnection.getResponseCode()).startsWith("20")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonString = sb.toString();
            System.out.println(jsonString);
        }

        return jsonString;
    }

    public static String postJSONObjectToURL(String urlString, String body) throws IOException, JSONException {
        return  postJSONObjectToURL(urlString, null, body);
    }

}
