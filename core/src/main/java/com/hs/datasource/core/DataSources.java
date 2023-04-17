package com.hs.datasource.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.DataSourceConst;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.core.data.Configurations;
import com.hs.datasource.core.utils.LoadUtil;

public class DataSources {

    /**
     * 根据配置实例化
     */
    public static DataSource newInstance(ObjectNode config) {
        String pluginName = config.get(DataSourceKey.NAME).asText();
        String className = config.get(DataSourceKey.CLASS).asText();
        DataSource dataSource = LoadUtil.loadPlugin(pluginName, className);
        dataSource.setConfig(config);
        JarLoader jarLoader = LoadUtil.getClassLoader(pluginName);
        Thread.currentThread().setContextClassLoader(jarLoader);
        return dataSource;
    }

    public static DataSource newInstance(String filename) {
        // 判断文件后缀
        if (!filename.endsWith(DataSourceConst.FILE_SUFFIX)) {
            filename += DataSourceConst.FILE_SUFFIX;
        }

        String datasource = filename.substring(0, filename.length() -
                DataSourceConst.FILE_SUFFIX.length());
        ObjectNode config = Configurations.read(filename);
        config.put(DataSourceKey.DATASOURCE, datasource);
        String pluginName = config.get(DataSourceKey.NAME).asText();
        ObjectNode plugin = Plugins.read(pluginName);
        config.put(DataSourceKey.CLASS, plugin.get(DataSourceKey.CLASS).asText());
        return newInstance(config);
    }
}
