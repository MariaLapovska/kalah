package com.backbase.kalah.mapper;

import com.backbase.kalah.exception.GameParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.log4j.Log4j2;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<Map<Integer, Integer>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MapType mapType =
            TypeFactory.defaultInstance().constructMapType(HashMap.class, Integer.class, Integer.class);

    @Override
    public String convertToDatabaseColumn(Map<Integer, Integer> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            log.error("Failed to write the game board as JSON to database", ex);
            throw new GameParsingException("Failed to save game");
        }
    }

    @Override
    public Map<Integer, Integer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, mapType);
        } catch (IOException ex) {
            log.error("Failed to read the game board as JSON from database", ex);
            throw new GameParsingException("Failed to read game");
        }
    }
}
