package com.github.kegszool.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
@Log4j2
public class JsonParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String parse(String JSON, String fieldName) throws JsonFieldNotFoundException, InvalidJsonFormatException {
        try {
            JsonNode rootNode = objectMapper.readTree(JSON);
            return processFieldValue(rootNode, fieldName, JSON);
        } catch(JsonProcessingException e) {
            handleInvalidJsonFormatException(JSON);
        }
        return null;
    }

    private String processFieldValue(JsonNode rootNode, String fieldName, String JSON) throws JsonFieldNotFoundException{
        String result = findFieldValue(rootNode, fieldName);
        if(result == null) {
            handleJsonFieldNotFoundException(fieldName, JSON);
        }
        return result;
    }

    private String findFieldValue(JsonNode node, String fieldName) {

        Stack<JsonNode> stack = new Stack<>();
        stack.push(node);

        while(!stack.isEmpty()) {
            JsonNode currentNode = stack.pop();

            if(currentNode.isObject() && currentNode.has(fieldName)) {
                return currentNode.get(fieldName).asText();
            }
            else if (currentNode.isObject() || currentNode.isArray()) {
                currentNode.forEach(stack::push);
            }
        }
        return null;
    }

    private void handleJsonFieldNotFoundException(String fieldName, String JSON) throws JsonFieldNotFoundException {
        log.warn("This JSON does not contain the field: \"{}\". " +
                "JSON:\n\t\t {}", fieldName, JSON);
        throw new JsonFieldNotFoundException(String.format("Field: \"%s\", JSON: %s", fieldName, JSON));
    }

    private void handleInvalidJsonFormatException(String JSON) throws InvalidJsonFormatException {
        log.error("JSON parsing ERROR");
        throw new InvalidJsonFormatException("JSON: " + JSON);
    }
}