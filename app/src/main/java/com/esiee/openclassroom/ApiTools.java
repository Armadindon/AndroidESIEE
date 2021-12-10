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

    public static String token = "";

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
        if (("" + urlConnection.getResponseCode()).startsWith("20")) {
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

    public static String postJSONObjectToURL(String urlString, String body) throws IOException, JSONException {
        return  postJSONObjectToURL(urlString, null, body);
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

    public static User getUserByUsername(String token, String username){
        String baseUrl = BuildConfig.API_URL;
        String usersString = "";
        try {
            usersString = getJSONObjectFromURL(baseUrl + "users?username="+username, token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        User[] users = null;
        try{
            users = mapper.readValue(usersString, User[].class);
            if(users.length == 0) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return users[0];
    }

    public static String authenticate(String user, String password){
        String baseUrl = BuildConfig.API_URL;
        String token = "";

        try {
            JSONObject json = new JSONObject();
            json.put("username", user);
            json.put("password", password);

            String userToken = postJSONObjectToURL(baseUrl + "authentication_token", json.toString());
            JSONObject tokenObject = new JSONObject(userToken);
            token = tokenObject.getString("token");
            ApiTools.token = token; //Degueu, mais pas de solution autre pour le moment
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static User registerUser(User u){
        String baseUrl = BuildConfig.API_URL;
        User newUser = null;
        try {
            ObjectMapper o = new ObjectMapper();
            String userJson = o.writeValueAsString(u);
            System.out.println(userJson);
            String createdUser = postJSONObjectToURL(baseUrl + "users", userJson);
            //On map l'user
            System.out.println(createdUser);
            newUser = o.readValue(createdUser, User.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public static Question createQuestion(Question q, String token){
        String baseUrl = BuildConfig.API_URL;
        Question newQuestion = null;
        try {
            ObjectMapper o = new ObjectMapper();
            String questionJson = o.writeValueAsString(q);
            System.out.println(questionJson);
            String createdQuestion = postJSONObjectToURL(baseUrl + "questions",token, questionJson);
            //On map la question
            System.out.println(createdQuestion);
            newQuestion = o.readValue(createdQuestion, Question.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newQuestion;
    }

    public static Score[] getScores(String token){
        String baseUrl = BuildConfig.API_URL;
        String scoreString = "";
        try {
            scoreString = getJSONObjectFromURL(baseUrl + "scores", token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        Score[] scores = null;
        try{
            scores = mapper.readValue(scoreString, Score[].class);
            if(scores.length == 0) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(Arrays.toString(scores));
        return scores;
    }
}
