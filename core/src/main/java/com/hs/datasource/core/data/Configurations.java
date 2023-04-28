package com.hs.datasource.core.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.DataSourceErrorCode;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.JsonUtil;
import com.hs.datasource.common.utils.StringUtil;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class Configurations {

    public static ArrayNode read() {
        File file = new File(DataSourceConst.CONF_PATH);
        File[] configs = file.listFiles();
        ArrayNode array = JsonUtil.createArrayNode();
        for (File config : configs) {
            array.add(JsonUtil.read(config.getPath()));
        }
        return array;
    }

    public static ObjectNode read(String filename) {
        return (ObjectNode) JsonUtil.read(getPath(filename));
    }

    public static ArrayNode readNames() {
        File file = new File(DataSourceConst.CONF_PATH);
        File[] pluginFiles = file.listFiles();
        ArrayNode arrayNode = JsonUtil.createArrayNode();
        for (File pluginFile : pluginFiles) {
            arrayNode.add(pluginFile.getName());
        }
        return arrayNode;
    }

    public static void write(String filename, ObjectNode config) {
        JsonUtil.write(getPath(filename), config);
    }

    public static String getPath(String filename) {
        return DataSourceConst.CONF_PATH + File.separator + filename;
    }

    public static void remove(String filename) {
        File file = new File(getPath(filename));
        if (file.exists()) {
            file.delete();
        }
    }

    public static void validateRequiredConfig(ObjectNode config, ObjectNode plugin) {
        ObjectNode configAttributes = (ObjectNode) config.get(DataSourceKey.ATTRIBUTES);
        ObjectNode pluginAttributes = (ObjectNode) plugin.get(DataSourceKey.ATTRIBUTES);
        validateRequiredAttributes(configAttributes, pluginAttributes);
    }

    public static void validateRequired(ObjectNode object, String key) {
        JsonNode node = object.get(key);
        if (node == null || StringUtil.isBlank(node.asText())) {
            throw CommonException.asException(
                    DataSourceErrorCode.REQUIRED_VALUE,
                    String.format("您提供配置文件有误，[%s]是必填参数，不允许为空或者留白.", key));
        }
    }

    public static void validateRequiredAttributes(ObjectNode attributes, ObjectNode attributesTemplate) {
        Iterator<Map.Entry<String, JsonNode>> pluginAttrIterator = attributesTemplate.fields();
        while (pluginAttrIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = pluginAttrIterator.next();
            String attribute = entry.getKey();
            ObjectNode value = (ObjectNode) entry.getValue();
            boolean required = value.get(DataSourceKey.REQUIRED).asBoolean();
            if (required) {
                validateRequired(attributes, attribute);
            }
        }
    }

}
