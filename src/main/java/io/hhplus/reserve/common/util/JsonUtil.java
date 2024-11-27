package io.hhplus.reserve.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON String to Object
     */
    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
        T object;
        try {
            object = objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return object;
    }

    /**
     * Object to JSON String
     */
    public static <T> String objectToJsonString(T object) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    /**
     * Map to JSON String
     */
    public static String mapToJsonString(Map<String, Object> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(map);
    }

    /**
     * List<Map> to JSON String
     */
    public static String listOfMapToJsonString(List<Map<String, Object>> list)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }

    /**
     * JSON String to Map
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * JSON String to List
     */
    public static <T> List<T> jsonStringToList(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                jsonString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
        );
    }

    /**
     * JSON Object to Map<String, String>
     */
    public static Map<String, String> jsonObjectToMap(Map<String, Object> jsonObject) {
        return jsonObject.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue().toString()
                ));
    }

    /**
     * JSON Array to List<Map<String, String>>
     */
    public static List<Map<String, String>> jsonArrayToListOfMap(String jsonArrayString) throws IOException {
        return objectMapper.readValue(
                jsonArrayString,
                TypeFactory.defaultInstance().constructCollectionType(
                        List.class,
                        TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class)
                )
        );
    }
}
