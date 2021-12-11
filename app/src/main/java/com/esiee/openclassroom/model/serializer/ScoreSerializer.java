package com.esiee.openclassroom.model.serializer;

import com.esiee.openclassroom.model.Score;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ScoreSerializer extends StdSerializer<Score> {

    public static String URL_USER = "/users/";


    public ScoreSerializer() {
        this(null);
    }

    public ScoreSerializer(Class<Score> t) {
        super(t);
    }

    @Override
    public void serialize(Score value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("score", value.getScore());
        jgen.writeStringField("byUser", URL_USER + value.getByUser().getIdForUs());

        jgen.writeEndObject();
    }
}
