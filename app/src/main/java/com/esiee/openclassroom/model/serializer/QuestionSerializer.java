package com.esiee.openclassroom.model.serializer;

import com.esiee.openclassroom.model.Question;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class QuestionSerializer extends StdSerializer<Question> {

    public static String URL_USER = "/users/";

    public QuestionSerializer() {
        this(null);
    }

    public QuestionSerializer(Class<Question> t) {
        super(t);
    }

    @Override
    public void serialize(
            Question value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("content", value.getContent());
        jgen.writeStringField("answer1", value.getAnswer1());
        jgen.writeStringField("answer2", value.getAnswer2());
        jgen.writeStringField("answer3", value.getAnswer3());
        jgen.writeStringField("answer4", value.getAnswer4());
        jgen.writeNumberField("answerIndex", value.getAnswerIndex());
        jgen.writeStringField("creator", URL_USER+value.getCreator().getIdForUs());

        jgen.writeEndObject();
    }
}