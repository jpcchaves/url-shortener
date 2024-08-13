package com.challenge.urlshortener.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;

public class SingletonObjectMapperConfig extends ObjectMapper {

  private static final ObjectMapper INSTANCE = new ObjectMapper();

  private SingletonObjectMapperConfig() {}

  public static ObjectMapper getInstance() {

    SimpleModule module = new SimpleModule();

    module.addDeserializer(
        LocalDateTime.class,
        CustomLocalDateTimeSerializableConfig
            .CUSTOM_LOCAL_DATE_TIME_DESERIALIZER);

    module.addSerializer(
        LocalDateTime.class,
        CustomLocalDateTimeSerializableConfig
            .CUSTOM_LOCAL_DATE_TIME_SERIALIZER);

    module.addDeserializer(
        LocalDate.class,
        CustomLocalDateTimeSerializableConfig.CUSTOM_LOCAL_DATE_DESERIALIZER);

    module.addSerializer(
        LocalDate.class,
        CustomLocalDateTimeSerializableConfig.CUSTOM_LOCAL_DATE_SERIALIZER);

    INSTANCE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    INSTANCE.registerModule(module);

    return INSTANCE;
  }
}
