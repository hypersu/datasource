package com.hs.datasource.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceErrorCode;
import com.hs.datasource.common.exception.CommonException;

import java.io.File;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static void putNotNull(ObjectNode node, String key, String value) {
        if (key == null || node == null || value == null) {
            return;
        }
        node.put(key, value);
    }

    public static void putNotNull(ObjectNode node, String key, Integer value) {
        if (key == null || node == null || value == null) {
            return;
        }
        node.put(key, value);
    }

    public static JsonNode read(String path) {
        File file = new File(path);
        return read(file);
    }

    public static JsonNode read(File file) {
        try {
            return mapper.readTree(file);
        } catch (IOException e) {
            throw CommonException.asException(DataSourceErrorCode.IO_EXCEPTION, e);
        }
    }

    public static void write(String path, JsonNode node) {
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            writer.writeValue(file, node);
        } catch (IOException e) {
            throw CommonException.asException(DataSourceErrorCode.IO_EXCEPTION, e);
        }
    }

    public static String asText(JsonNode node) {
        if (node == null) {
            return null;
        }
        return node.asText();
    }

    public static String getString(JsonNode node, String fieldName) {
        if (node == null) {
            return null;
        }
        JsonNode field = node.get(fieldName);
        return asText(field);
    }

}
