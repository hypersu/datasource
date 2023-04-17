package com.hs.datasource.core;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.utils.JsonUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Plugins {
    private static final Map<String, ObjectNode> PLUGINS = new ConcurrentHashMap<>();

    public static ObjectNode read(String name) {
        ObjectNode plugin = PLUGINS.get(name);
        if (plugin == null) {
            String path = getPath(name) + File.separator + "plugin.json";
            plugin = (ObjectNode) JsonUtil.read(path);
            PLUGINS.put(name, plugin);
        }
        return plugin;
    }

    public static ObjectNode readMeta(String name) {
        ObjectNode plugin = read(name);
        return (ObjectNode) plugin.get(DataSourceKey.META);
    }

    public static ObjectNode readTemplate(String name) {
        return (ObjectNode) JsonUtil.read(getPath(name) + File.separator + "plugin_template.json");
    }

    public static ArrayNode readNames() {
        File file = new File(DataSourceConst.PLUGIN_PATH);
        File[] pluginFiles = file.listFiles();
        ArrayNode arrayNode = JsonUtil.createArrayNode();
        for (File pluginFile : pluginFiles) {
            arrayNode.add(pluginFile.getName());
        }
        return arrayNode;
    }

    public static String getPath(String name) {
        return DataSourceConst.PLUGIN_PATH + File.separator + name;
    }
}
