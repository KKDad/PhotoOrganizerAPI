package org.stapledon.photo.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class DateJsonDeserializer extends JsonDeserializer {

    private static DateTimeFormatter formatter = ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        return LocalDateTime.from(formatter.parse(node.textValue()));
    }
}
