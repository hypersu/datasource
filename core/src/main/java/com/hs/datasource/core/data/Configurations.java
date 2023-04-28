package com.hs.datasource.core.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.DataSourceErrorCode;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.JsonUtil;
import com.hs.datasource.core.Plugins;

import java.io.File;

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

    public static void validateRequired(ObjectNode conf, String key) {
        String pluginName = conf.get(DataSourceKey.NAME).asText();
        ObjectNode attribute = Plugins.readAttributes(pluginName, key);
        boolean required = attribute.get(DataSourceKey.REQUIRED).asBoolean();
        JsonNode node = conf.get(key);
        if (required) {
            if (node == null) {
                throw CommonException.asException(
                        DataSourceErrorCode.REQUIRED_VALUE,
                        String.format("您提供配置文件有误，[%s]是必填参数，不允许为空或者留白.", key));
            }
        }
    }

    public static String asString(ObjectNode conf, String key) {
        validateRequired(conf, key);
        JsonNode node = conf.get(key);

        if (node == null || node.asText() == null) {
            return null;
        }

        return node.asText();
    }

    public static Integer asInteger(ObjectNode conf, String key) {
        validateRequired(conf, key);
        JsonNode node = conf.get(key);

        if (node == null) {
            return null;
        }

        return node.asInt();
    }

    public static ObjectNode asObject(ObjectNode conf, String key) {
        validateRequired(conf, key);
        return (ObjectNode) conf.get(key);
    }
}
