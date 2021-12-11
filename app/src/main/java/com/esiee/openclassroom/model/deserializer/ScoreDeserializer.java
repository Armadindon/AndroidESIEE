package com.esiee.openclassroom.model.deserializer;

import com.esiee.openclassroom.ApiTools;
import com.esiee.openclassroom.BuildConfig;
import com.esiee.openclassroom.DataManager;
import com.esiee.openclassroom.model.Question;
import com.esiee.openclassroom.model.Score;
import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.json.JSONException;

import java.io.IOException;

public class ScoreDeserializer extends StdDeserializer<Score> {

    public ScoreDeserializer() {
        this(null);
    }

    public ScoreDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Score deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String baseUrl = BuildConfig.API_URL;
        JsonNode node = p.getCodec().readTree(p);
        System.out.println("Deserialization");
        int score = node.get("score").asInt();
        System.out.println("Deserialization score");
        String userURI = node.get("byUser").asText();
        System.out.println("Deserialization user");
        User user = null;
        try {
            String userJSON = ApiTools.getJSONObjectFromURL(baseUrl+userURI.substring(1), DataManager.getInstance().getToken());
            ObjectMapper ob = new ObjectMapper();
            user = ob.readValue(userJSON, User.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Score s = new Score(score, user);
        s.setId(node.get("id").asInt());

        return s;
    }
}