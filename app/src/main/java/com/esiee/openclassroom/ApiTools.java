package com.esiee.openclassroom;

import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiTools {

    static String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2Mzg3OTA0OTIsImV4cCI6MTYzODc5NDA5Miwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInVzZXJuYW1lIjoidmluYyJ9.wiPOS0MrCEORYYaThizZ2btQ2QePZwdfEoLHMaueqWU-NF42RE3aojzjmgkKInoO9Fyj1L4uCOXy8OxJ5do9trDk1ho82_1H-DEWqMU89nSXelTDKCAZeklZTUGTKKfCnI8Q1aj-B_d7cvJyOVNZCDI-9MFxi1SXnaNT16eAEpABZADajceeh945pulEim62uo4Rex7HXPq3uVKKhgBoIY9D2dFWFMe08O_WfLQO5ZZ_DGCcKM-CMXt_UEVKvdBm0rMJ3savIOMalDOKwaICpBF1dzDfgRnf4ExkznsX_nTCAfRQb4cmyDpDxW5A6q8r6lhpaW-Rsn0PWxcW69tcGQ";
    static String user = "{\"username\": \"vinc\",\"password\": \"changeme\" }";
    // Make a GET Request
    public static String getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        System.out.println("HTTP REQUEST");
        HttpsURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", token);
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
        System.out.println("HTTP POST");
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

        System.out.println(urlConnection.getResponseCode());
        System.out.println(urlConnection.getResponseMessage());

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
        System.out.println(jsonString);
        return jsonString;
    }

    public static User[] getAllUsers(){
        String baseUrl = BuildConfig.API_URL;
        String usersString = "";
        try {
            usersString = getJSONObjectFromURL(baseUrl + "users");
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
        String userToken = "";
        try {
            userToken = postJSONObjectToURL(baseUrl + "authentication_token");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userToken;
    }


}
