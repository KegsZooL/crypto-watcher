package com.github.kegszool.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class JsonParser {

    //TODO: добавить обработку исключений + попробывать написать без null

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String parse(String JSON, String fieldName) {
        try {
            JsonNode rootNode = objectMapper.readTree(JSON);
            return findFieldValue(rootNode, fieldName);
        } catch(JsonProcessingException e) {

        }
        return null;
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
}