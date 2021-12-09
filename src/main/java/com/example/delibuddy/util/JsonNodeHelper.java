package com.example.delibuddy.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class JsonNodeHelper {

    public static String extractTextOrNull(JsonNode node, List<String> fields) {
        int i = 0;
        while (node != null && i < fields.size()) {
            node = node.get(fields.get(i));
            i++;
        }
        return node != null ? node.asText() : null;
    }

}
