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
        // 校验 name,datasource
        Configurations.validateRequired(config, DataSourceKey.NAME);
        Configurations.validateRequired(config, DataSourceKey.DATASOURCE);

        // 获取 plugin
        String pluginName = config.get(DataSourceKey.NAME).asText();
        ObjectNode plugin = Plugins.read(pluginName);
        String className = plugin.get(DataSourceKey.CLASS).asText();
        config.put(DataSourceKey.CLASS, className);

        // 校验 config
        Configurations.validateRequiredConfig(config, plugin);

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
        return newInstance(config);
    }
}
