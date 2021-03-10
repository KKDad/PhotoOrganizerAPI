package org.stapledon.photo.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class DateJsonSerializer extends StdSerializer<LocalDateTime> {

        private static DateTimeFormatter formatter = ISO_LOCAL_DATE_TIME;

        public DateJsonSerializer() {
            this(null);
        }

        public DateJsonSerializer(Class<LocalDateTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider arg2)  throws IOException
        {
            gen.writeString(formatter.format(value));
        }
    }