package com.hs.datasource.core.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.utils.PluginUtil;
import com.hs.datasource.common.utils.StringPool;
import com.hs.datasource.core.data.MetaDataTreeFilter;

import java.io.File;

public class MetaUtil {
    private static final MetaDataTreeFilter filter = new MetaDataTreeFilter();

    public static String[] arrayHierarchy(String hierarchy) {
        return hierarchy.split("\\.");
    }

    public static String getPath(String datasource) {
        return DataSourceConst.META_PATH + File.separator + datasource;
    }

    public static String getPath(String datasource, String hierarchy, ObjectNode meta) {
        return getPath(datasource, arrayHierarchy(hierarchy), meta);
    }

    public static String getPath(String datasource, String[] hierarchies, ObjectNode meta) {
        StringBuilder path = new StringBuilder();
        path.append(DataSourceConst.META_PATH);
        path.append(File.separator);
        path.append(datasource);
        for (int i = 0; i < hierarchies.length; i++) {
            String part = hierarchies[i];
            String pathPart = PluginUtil.getString(part, meta);
            path.append(File.separator);
            path.append(pathPart);
            if (i == hierarchies.length - 1) {
                path.append(DataSourceConst.FILE_SUFFIX);
            }
        }
        return path.toString();
    }

    public static String convertToPath(String datasource, String name) {
        return convertToDir(datasource, name) + DataSourceConst.FILE_SUFFIX;
    }

    public static String convertToDir(String datasource, String name) {
        name = name.replace(StringPool.DOT, File.separator);
        return getPath(datasource) + File.separator + name;
    }

    public static void tree(ArrayNode array, File root) {
        if (root == null || !root.exists()
                || !root.isDirectory()) {
            return;
        }
        File[] files = root.listFiles(filter);
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            ObjectNode object = array.addObject();
            ArrayNode sub = object.putArray(file.getName());
            tree(sub, file);
        }
    }
}
