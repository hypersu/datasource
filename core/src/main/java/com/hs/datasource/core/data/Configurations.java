package com.hs.datasource.core.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.utils.JsonUtil;

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

    public static void write(String filename, JsonNode config) {
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
}
