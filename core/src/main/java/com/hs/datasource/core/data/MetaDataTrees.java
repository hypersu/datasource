package com.hs.datasource.core.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.core.utils.MetaUtil;
import com.hs.datasource.common.utils.FileUtil;
import com.hs.datasource.common.utils.JsonUtil;
import com.hs.datasource.core.DataSourceFilter;

import java.io.File;

public class MetaDataTrees {

    /**
     * 读取具体的节点信息
     *
     * @param datasource 数据源名称
     * @param name       层级名称
     */
    public static ObjectNode readOne(String datasource, String name) {
        String path = MetaUtil.convertToPath(datasource, name);
        return (ObjectNode) JsonUtil.read(path);
    }

    /**
     * 读取元数据树
     */
    public static ArrayNode readAll(String datasource) {
        String path = MetaUtil.getPath(datasource);
        File file = new File(path);
        ArrayNode array = JsonUtil.createArrayNode();
        MetaUtil.tree(array, file);
        return array;
    }

    /**
     * 读取某节点下所有子节点信息
     */
    public static ArrayNode readAll(String datasource, String name) {
        String path = MetaUtil.convertToDir(datasource, name);
        File file = new File(path);
        File[] configs = file.listFiles(DataSourceFilter.getInstance());
        ArrayNode array = JsonUtil.createArrayNode();
        for (File config : configs) {
            array.add(JsonUtil.read(config));
        }
        return array;
    }

    public static void writeOne(String datasource, String hierarchy, ObjectNode object) {
        String path = MetaUtil.getPath(datasource, hierarchy, object);
        JsonUtil.write(path, object);
    }

    public static void writeOne(String datasource, String[] hierarchies, ObjectNode object) {
        String path = MetaUtil.getPath(datasource, hierarchies, object);
        JsonUtil.write(path, object);
    }

    public static void write(String datasource, String[] hierarchies, ArrayNode array) {
        if (array == null) {
            return;
        }
        for (JsonNode node : array) {
            ObjectNode object = (ObjectNode) node;
            writeOne(datasource, hierarchies, object);
        }
    }

    /**
     * 删除数据源下元数据
     *
     * @param datasource 数据源名称
     */
    public static void removeAll(String datasource) {
        String path = MetaUtil.getPath(datasource);
        FileUtil.deleteDirectory(path);
    }

    /**
     * 删除某一节点下所有元数据
     */
    public static void removeAll(String datasource, String name) {
        String dir = MetaUtil.convertToDir(datasource, name);
        String path = MetaUtil.convertToPath(datasource, name);
        FileUtil.deleteFile(path);
        FileUtil.deleteDirectory(dir);
    }

    /**
     * 删除某一节点元数据
     *
     * @param datasource 数据源名称
     * @param name       层级名称
     */
    public static void removeOne(String datasource, String name) {
        String path = MetaUtil.convertToPath(datasource, name);
        FileUtil.deleteFile(path);
    }

}
