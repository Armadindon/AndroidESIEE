package com.esiee.openclassroom.model.deserializer;

import com.esiee.openclassroom.ApiTools;
import com.esiee.openclassroom.BuildConfig;
import com.esiee.openclassroom.DataManager;
import com.esiee.openclassroom.model.Question;
import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.json.JSONException;

import java.io.IOException;

public class QuestionDeserializer extends StdDeserializer<Question> {

    public QuestionDeserializer() {
        this(null);
    }

    public QuestionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Question deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String baseUrl = BuildConfig.API_URL;
        JsonNode node = p.getCodec().readTree(p);
        String content = node.get("content").asText();
        String answer1 = node.get("answer1").asText();;
        String answer2 = node.get("answer2").asText();
        String answer3 = node.get("answer3").asText();
        String answer4 = node.get("answer4").asText();
        int answerIndex = node.get("answerIndex").asInt();
        String creatorURI = node.get("creator").asText();
        User creator = null;
        try {
            String creatorJSON = ApiTools.getJSONObjectFromURL(baseUrl+creatorURI.substring(1), DataManager.getInstance().getToken());
            ObjectMapper ob = new ObjectMapper();
            creator = ob.readValue(creatorJSON, User.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Question q = new Question(content, answer1, answer2, answer3, answer4, answerIndex, creator);
        q.setId(node.get("id").asInt());

        return q;
    }
}
