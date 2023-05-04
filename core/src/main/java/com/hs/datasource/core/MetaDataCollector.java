package com.hs.datasource.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.ConnectionAdapter;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.MetaDataAdapter;
import com.hs.datasource.core.data.MetaDataTrees;
import com.hs.datasource.core.utils.MetaUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetaDataCollector {

    /**
     * 采集元数据
     */
    public static void collect(DataSource dataSource) {
        ObjectNode config = dataSource.getConfig();
        String sourceName = config.get(DataSourceKey.DATASOURCE).asText();
        String pluginName = config.get(DataSourceKey.NAME).asText();

        // 元数据信息
        ObjectNode pluginMeta = Plugins.readMeta(pluginName);
        ArrayNode metaTypes = (ArrayNode) pluginMeta.get(DataSourceKey.TYPE);
        log.info("正在采集元数据...");
        //获取适配器
        ConnectionAdapter conn = dataSource.getConnection();
        try {
            MetaDataAdapter meta = conn.getMetaData();
            for (JsonNode node : metaTypes) {
                String metaType = node.asText();
                String hierarchy = pluginMeta.get(metaType)
                        .get(DataSourceKey.HIERARCHY).asText();
                String[] hierarchies = MetaUtil.arrayHierarchy(hierarchy);
                ArrayNode metaNodes = meta.get(metaType);
                MetaDataTrees.write(sourceName, hierarchies, metaNodes);
            }
        } finally {
            log.info("采集元数据结束.");
            conn.close();
        }
    }

    public static void collect(DataSource dataSource, ObjectNode metaNode) {
        ObjectNode config = dataSource.getConfig();
        String sourceName = config.get(DataSourceKey.DATASOURCE).asText();
        String pluginName = config.get(DataSourceKey.NAME).asText();

        // 元数据信息
        ObjectNode pluginMeta = Plugins.readMeta(pluginName);
        ArrayNode metaTypes = (ArrayNode) pluginMeta.get(DataSourceKey.TYPE);
        log.info("正在采集元数据...");
        //获取适配器
        ConnectionAdapter conn = dataSource.getConnection();
        try {
            MetaDataAdapter meta = conn.getMetaData();
            for (JsonNode node : metaTypes) {
                String metaType = node.asText();
                String hierarchy = pluginMeta.get(metaType)
                        .get(DataSourceKey.HIERARCHY).asText();
                String[] hierarchies = MetaUtil.arrayHierarchy(hierarchy);
                ArrayNode metaNodes = meta.get(metaType, metaNode);
                MetaDataTrees.write(sourceName, hierarchies, metaNodes);
            }
        } finally {
            log.info("采集元数据结束.");
            conn.close();
        }
    }

    /**
     * 采集特定类型的元数据
     */
    public static void collect(DataSource dataSource, String type) {
        ObjectNode config = dataSource.getConfig();
        String sourceName = config.get(DataSourceKey.DATASOURCE).asText();
        String pluginName = config.get(DataSourceKey.NAME).asText();

        // 元数据信息
        ObjectNode pluginMeta = Plugins.readMeta(pluginName);
        log.info("正在采集元数据...");
        //获取适配器
        ConnectionAdapter conn = dataSource.getConnection();
        try {
            MetaDataAdapter meta = conn.getMetaData();
            String hierarchy = pluginMeta.get(type)
                    .get(DataSourceKey.HIERARCHY).asText();
            String[] hierarchies = MetaUtil.arrayHierarchy(hierarchy);
            ArrayNode metaNodes = meta.get(type);
            MetaDataTrees.write(sourceName, hierarchies, metaNodes);
        } finally {
            log.info("采集元数据结束.");
            conn.close();
        }
    }

    /**
     * 采集特定类型的元数据
     */
    public static void collect(DataSource dataSource, String type, ObjectNode metaNode) {
        ObjectNode config = dataSource.getConfig();
        String sourceName = config.get(DataSourceKey.DATASOURCE).asText();
        String pluginName = config.get(DataSourceKey.NAME).asText();

        // 元数据信息
        ObjectNode pluginMeta = Plugins.readMeta(pluginName);
        log.info("正在采集元数据...");
        //获取适配器
        ConnectionAdapter conn = dataSource.getConnection();
        try {
            MetaDataAdapter meta = conn.getMetaData();
            String hierarchy = pluginMeta.get(type)
                    .get(DataSourceKey.HIERARCHY).asText();
            String[] hierarchies = MetaUtil.arrayHierarchy(hierarchy);
            ArrayNode metaNodes = meta.get(type, metaNode);
            MetaDataTrees.write(sourceName, hierarchies, metaNodes);
        } finally {
            log.info("采集元数据结束.");
            conn.close();
        }
    }

}
