package com.challenge.urlshortener.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonGenerator;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParser;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonDeserializer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonSerializer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializerProvider;

public class CustomLocalDateTimeSerializable {

  public static CustomLocalDateTimeSerializer
      CUSTOM_LOCAL_DATE_TIME_SERIALIZER = new CustomLocalDateTimeSerializer();

  public static CustomLocalDateTimeDeserializer
      CUSTOM_LOCAL_DATE_TIME_DESERIALIZER =
          new CustomLocalDateTimeDeserializer();

  public static class CustomLocalDateTimeSerializer
      extends JsonSerializer<LocalDateTime> {

    private CustomLocalDateTimeSerializer() {
      super();
    }

    @Override
    public void serialize(
        LocalDateTime localDateTime,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider)
        throws IOException, JsonProcessingException {

      jsonGenerator.writeString(
          localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }
  }

  public static class CustomLocalDateTimeDeserializer
      extends JsonDeserializer<LocalDateTime> {

    private CustomLocalDateTimeDeserializer() {
      super();
    }

    @Override
    public LocalDateTime deserialize(
        JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
      return LocalDateTime.parse(
          jsonParser.getText(), DateTimeFormatter.ISO_DATE_TIME);
    }
  }
}
